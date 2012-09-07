package net.ess3.utils.textreader;

import java.io.*;
import java.lang.ref.SoftReference;
import java.util.*;
import java.util.logging.Level;
import net.ess3.api.IEssentials;
import net.ess3.api.IUser;
import net.ess3.api.InvalidNameException;
import net.ess3.utils.Util;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class TextInput implements IText
{
	private final transient List<String> lines;
	private final transient List<String> chapters;
	private final transient Map<String, Integer> bookmarks;
	private final transient long lastChange;
	private final static HashMap<String, SoftReference<TextInput>> cache = new HashMap<String, SoftReference<TextInput>>();

	public TextInput(final CommandSender sender, final String filename, final boolean createFile, final IEssentials ess) throws IOException
	{

		File file = null;
		if (sender instanceof Player)
		{
			try
			{
				final IUser user = ess.getUserMap().getUser((Player)sender);
				file = new File(ess.getPlugin().getDataFolder(), filename + "_" + Util.sanitizeFileName(user.getName()) + ".txt");
				if (!file.exists())
				{
					file = new File(ess.getPlugin().getDataFolder(), filename + "_" + Util.sanitizeFileName(ess.getRanks().getMainGroup(user)) + ".txt");
				}
			}
			catch (InvalidNameException ex)
			{
				ess.getLogger().log(Level.WARNING, ex.getMessage(), ex);
			}
		}
		if (file == null || !file.exists())
		{
			file = new File(ess.getPlugin().getDataFolder(), filename + ".txt");
		}
		if (file.exists())
		{
			lastChange = file.lastModified();
			boolean readFromfile;
			synchronized (cache)
			{
				final SoftReference<TextInput> inputRef = cache.get(file.getName());
				TextInput input;
				if (inputRef == null || (input = inputRef.get()) == null || input.lastChange < lastChange)
				{
					lines = new ArrayList<String>();
					chapters = new ArrayList<String>();
					bookmarks = new HashMap<String, Integer>();
					cache.put(file.getName(), new SoftReference<TextInput>(this));
					readFromfile = true;
				}
				else
				{
					lines = Collections.unmodifiableList(input.getLines());
					chapters = Collections.unmodifiableList(input.getChapters());
					bookmarks = Collections.unmodifiableMap(input.getBookmarks());
					readFromfile = false;
				}
			}
			if (readFromfile)
			{
				final BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
				try
				{
					int lineNumber = 0;
					while (bufferedReader.ready())
					{
						final String line = bufferedReader.readLine();
						if (line == null)
						{
							break;
						}
						if (line.length() > 0 && line.charAt(0) == '#')
						{
							bookmarks.put(line.substring(1).toLowerCase(Locale.ENGLISH).replaceAll("&[0-9a-fk]", ""), lineNumber);
							chapters.add(line.substring(1).replace('&', '\u00a7').replace("\u00a7\u00a7", "&"));
						}
						lines.add(line.replace('&', '\u00a7').replace("\u00a7\u00a7", "&"));
						lineNumber++;
					}
				}
				finally
				{
					bufferedReader.close();
				}
			}
		}
		else
		{
			lastChange = 0;
			lines = Collections.emptyList();
			chapters = Collections.emptyList();
			bookmarks = Collections.emptyMap();
			if (createFile)
			{
				final InputStream input = ess.getPlugin().getResource(filename + ".txt");
				final OutputStream output = new FileOutputStream(file);
				try
				{
					final byte[] buffer = new byte[1024];
					int length = input.read(buffer);
					while (length > 0)
					{
						output.write(buffer, 0, length);
						length = input.read(buffer);
					}
				}
				finally
				{
                    if(output != null)
					output.close();
                    if(input != null)
					input.close();
				}
				throw new FileNotFoundException("File " + filename + ".txt does not exist. Creating one for you.");
			}
		}
	}

	@Override
	public List<String> getLines()
	{
		return lines;
	}

	@Override
	public List<String> getChapters()
	{
		return chapters;
	}

	@Override
	public Map<String, Integer> getBookmarks()
	{
		return bookmarks;
	}
}
