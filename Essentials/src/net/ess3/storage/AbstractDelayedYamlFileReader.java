package net.ess3.storage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import net.ess3.api.IEssentials;
import org.bukkit.Bukkit;


public abstract class AbstractDelayedYamlFileReader<T extends StorageObject> implements Runnable
{
	private final Class<T> clazz;
	private final IEssentials ess;

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
			ess.getPlugin().scheduleAsyncDelayedTask(this);
		}
	}

	public abstract File onStart();

	@Override
	public void run()
	{
		final File file = onStart();
		synchronized (file)
		{
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
				Bukkit.getLogger().log(Level.INFO, "File not found: " + file.toString());
			}
			catch (ObjectLoadException ex)
			{
				onException(ex);
				File broken = new File(file.getAbsolutePath() + ".broken." + System.currentTimeMillis());
				file.renameTo(broken);
				Bukkit.getLogger().log(Level.SEVERE, "The file " + file.toString() + " is broken, it has been renamed to " + broken.toString(), ex.getCause());
			}
		}
	}

	public abstract void onSuccess(T object);

	public abstract void onException(Exception exception);
}
