package net.ess3.storage;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.util.concurrent.UncheckedExecutionException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.regex.Pattern;
import net.ess3.api.IEssentials;
import net.ess3.api.InvalidNameException;
import net.ess3.utils.Util;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.io.IOUtils;


public abstract class StorageObjectMap<I> extends CacheLoader<String, I> implements IStorageObjectMap<I>
{
	protected final transient IEssentials ess;
	private final transient File folder;
	protected final transient Cache<String, I> cache = CacheBuilder.newBuilder().softValues().build(this);
	protected final transient ConcurrentSkipListSet<String> keys = new ConcurrentSkipListSet<String>();
	protected final transient ConcurrentSkipListMap<String, File> zippedfiles = new ConcurrentSkipListMap<String, File>();
	private final Pattern zipCheck = Pattern.compile("^[a-zA-Z0-9]*-?[a-zA-Z0-9]+\\.yml$");

	public StorageObjectMap(final IEssentials ess, final String folderName)
	{
		super();
		this.ess = ess;
		this.folder = new File(ess.getPlugin().getDataFolder(), folderName);
		if (!folder.exists())
		{
			folder.mkdirs();
		}
		loadAllObjectsAsync();
	}

	private void loadAllObjectsAsync()
	{
		ess.getPlugin().scheduleAsyncDelayedTask(
				new Runnable()
				{
					@Override
					public void run()
					{
						if (!folder.exists() || !folder.isDirectory())
						{
							return;
						}
						keys.clear();
						cache.invalidateAll();
						for (String string : folder.list())
						{
							final File file = new File(folder, string);
							if (!file.isFile() || !file.canRead())
							{
								continue;
							}
							if (string.endsWith(".yml"))
							{
								addFileToKeys(string.substring(0, string.length() - 4));
							}
							if (string.endsWith(".zip"))
							{
								addZipFile(file);
							}
						}
					}

					private void addFileToKeys(String filename)
					{
						try
						{

							final String name = Util.decodeFileName(filename);
							keys.add(name.toLowerCase(Locale.ENGLISH));

						}
						catch (InvalidNameException ex)
						{
							ess.getLogger().log(Level.WARNING, "Invalid filename: " + filename, ex);
						}
					}

					private void addZipFile(File file)
					{
						try
						{
							final ZipFile zipFile = new ZipFile(file);
							try
							{
								final Enumeration<ZipArchiveEntry> entries = zipFile.getEntriesInPhysicalOrder();
								while (entries.hasMoreElements())
								{
									final ZipArchiveEntry entry = entries.nextElement();
									final String name = entry.getName();
									if (entry.isDirectory() || entry.getSize() == 0 || !zipCheck.matcher(name).matches())
									{
										continue;
									}
									final String shortName = name.substring(0, name.length() - 4);
									addFileToKeys(shortName);
									zippedfiles.put(name, file);
								}
							}
							finally
							{
								zipFile.close();
							}
						}
						catch (IOException ex)
						{
							ess.getLogger().log(Level.WARNING, "Error opening file " + file.getAbsolutePath(), ex);
						}
					}
				});
	}

	@Override
	public boolean objectExists(final String name)
	{
		return keys.contains(name.toLowerCase(Locale.ENGLISH));
	}

	@Override
	public I getObject(final String name)
	{
		try
		{
			return (I)cache.get(name);
		}
		catch (ExecutionException ex)
		{
			return null;
		}
		catch (UncheckedExecutionException ex)
		{
			return null;
		}
	}

	@Override
	public abstract I load(final String name) throws Exception;

	@Override
	public void removeObject(final String name) throws InvalidNameException
	{
		String lowerCaseName = name.toLowerCase(Locale.ENGLISH);
		keys.remove(lowerCaseName);
		cache.invalidate(lowerCaseName);
		final File file = getStorageFile(name);
		if (file.exists())
		{
			file.delete();
		}
		String sanitizedFilename = Util.sanitizeFileName(name) + ".yml";
		if (zippedfiles.containsKey(sanitizedFilename))
		{
			zippedfiles.put(sanitizedFilename, null);
		}
	}

	@Override
	public Set<String> getAllKeys()
	{
		return Collections.unmodifiableSet(keys);
	}

	@Override
	public int getKeySize()
	{
		return keys.size();
	}

	@Override
	public File getStorageFile(final String name) throws InvalidNameException
	{
		if (!folder.exists() || !folder.isDirectory())
		{
			throw new InvalidNameException(new IOException("Folder does not exists: " + folder));
		}
		String sanitizedFilename = Util.sanitizeFileName(name) + ".yml";
		File file = new File(folder, sanitizedFilename);

		if (!file.exists())
		{
			extractFileFromZip(sanitizedFilename, file);
		}
		return file;
	}

	@Override
	public void onReload()
	{
		loadAllObjectsAsync();
	}

	private void extractFileFromZip(String sanitizedFilename, File file)
	{
		File zipFile = zippedfiles.get(sanitizedFilename);
		if (zipFile != null)
		{
			try
			{
				ZipFile zip = new ZipFile(zipFile);
				try
				{
					ZipArchiveEntry entry = zip.getEntry(sanitizedFilename);
					if (entry != null)
					{
						try
						{
							IOUtils.copy(zip.getInputStream(entry), new FileOutputStream(file));
						}
						catch (IOException ex)
						{
							ess.getLogger().log(Level.WARNING, "Failed to write file: " + file.getAbsolutePath(), ex);
						}
					}
					else
					{
						ess.getLogger().log(Level.WARNING, "File " + file.getAbsolutePath() + " not found in zip file " + zipFile.getAbsolutePath());
					}
				}
				finally
				{
					zip.close();
				}
			}
			catch (IOException ex)
			{
				ess.getLogger().log(Level.WARNING, "File " + file.getAbsolutePath() + " could not be extracted from " + zipFile.getAbsolutePath(), ex);
			}
		}
	}
}
