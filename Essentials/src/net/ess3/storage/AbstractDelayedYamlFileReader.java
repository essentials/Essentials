package net.ess3.storage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import net.ess3.Essentials;
import net.ess3.api.IEssentials;
import org.bukkit.Bukkit;


public abstract class AbstractDelayedYamlFileReader<T extends StorageObject> implements Runnable
{
	
	private final transient Class<T> clazz;
	private final transient IEssentials ess;
	private final transient ReentrantLock lock = new ReentrantLock();

	public AbstractDelayedYamlFileReader(final IEssentials ess, final Class<T> clazz)
	{
		this.clazz = clazz;
		this.ess = ess;
	}

	public void schedule(boolean instant)
	{
		if (instant)
		{
			run();
		}
		else
		{
			ess.getPlugin().getServer().getScheduler().scheduleAsyncDelayedTask(ess.getPlugin(), this);
		}
	}

	public abstract File onStart() throws IOException;

	@Override
	public void run()
	{
		File file = null;
		lock.lock();
		try
		{
			file = onStart();
			try
			{
				final FileReader reader = new FileReader(file);
				try
				{
					final T object = new YamlStorageReader(reader, ess.getPlugin()).load(clazz);
					onSuccess(object);
				}
				finally
				{
					try
					{
						reader.close();
					}
					catch (IOException ex)
					{
						Bukkit.getLogger().log(Level.SEVERE, "File can't be closed: " + file.toString(), ex);
					}
				}

			}
			catch (FileNotFoundException ex)
			{
				onException(ex);
				Bukkit.getLogger().log(Level.INFO, "File not found: {0}", file.toString());
			}
			catch (ObjectLoadException ex)
			{
				onException(ex);
				File broken = new File(file.getAbsolutePath() + ".broken." + System.currentTimeMillis());
				file.renameTo(broken);
				Bukkit.getLogger().log(Level.SEVERE, "The file " + file.toString() + " is broken, it has been renamed to " + broken.toString(), ex.getCause());
			}
		}
		catch (IOException ex)
		{
			onException(ex);
			if (ess.getSettings() == null || ess.getSettings().isDebug())
			{
				Bukkit.getLogger().log(Level.INFO, "File not found: " + file.toString());
			}
		}
		finally
		{
			lock.unlock();
		}
	}

	public abstract void onSuccess(T object);

	public abstract void onException(Exception exception);
}
