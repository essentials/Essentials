package net.ess3.storage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import net.ess3.api.IEssentials;
import org.bukkit.Bukkit;


public abstract class AbstractDelayedYamlFileWriter implements Runnable
{
	private final transient IEssentials ess;
	private final transient ReentrantLock lock = new ReentrantLock();

	public AbstractDelayedYamlFileWriter(final IEssentials ess)
	{
		this.ess = ess;
	}

	public void schedule()
	{
		ess.getPlugin().scheduleAsyncDelayedTask(this);
	}

	public abstract File getFile();

	public abstract StorageObject getObject();

	@Override
	public void run()
	{
		final File file = getFile();
		synchronized (file)
		{
			PrintWriter pw = null;
			try
			{
				final StorageObject object = getObject();
				final File folder = file.getParentFile();
				if (!folder.exists())
				{
					folder.mkdirs();
				}
				pw = new PrintWriter(file);
				new YamlStorageWriter(pw).save(object);
			}
			catch (FileNotFoundException ex)
			{
				Bukkit.getLogger().log(Level.SEVERE, file.toString(), ex);
			}
			finally
			{
				onFinish();
				if (pw != null)
				{
					pw.close();
				}
			}
		}
	}

	public abstract void onFinish();
}
