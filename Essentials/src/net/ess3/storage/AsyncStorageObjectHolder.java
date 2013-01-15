package net.ess3.storage;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import net.ess3.api.IEssentials;
import org.bukkit.Bukkit;


public abstract class AsyncStorageObjectHolder<T extends StorageObject> implements IStorageObjectHolder<T>
{
	private transient T data;
	private final transient Class<T> clazz;
	protected final transient IEssentials ess;
	private final transient StorageObjectDataWriter writer;
	private final transient StorageObjectDataReader reader;
	private final transient AtomicBoolean loaded = new AtomicBoolean(false);
	private volatile long savetime = 0;
	private final transient File file;

	public AsyncStorageObjectHolder(final IEssentials ess, final Class<T> clazz, final File file)
	{
		this.ess = ess;
		this.clazz = clazz;
		this.file = file;
		writer = new StorageObjectDataWriter();
		reader = new StorageObjectDataReader();
		try
		{
			this.data = clazz.newInstance();
		}
		catch (Exception ex)
		{
			Bukkit.getLogger().log(Level.SEVERE, ex.getMessage(), ex);
		}
	}

	/**
	 * Warning: If you access this method, you have to acquire a read or write lock first
	 *
	 * @return Object storing all the data
	 */
	@Override
	public T getData()
	{
		if (!loaded.get())
		{
			reader.schedule(true);
		}
		return data;
	}

	@Override
	public void queueSave()
	{
		ess.getStorageQueue().queue(this);
	}

	@Override
	public void onReload()
	{
		onReload(true);
	}

	public void onReload(final boolean instant)
	{
		reader.schedule(instant);
	}

	@Override
	public String toString()
	{
		return file.getAbsolutePath();
	}

	protected void finishRead()
	{
	}

	protected void finishWrite()
	{
	}

	protected void fillWithDefaults()
	{
	}

	public StorageQueue.RequestState getRequestState(long timestamp)
	{
		if (savetime == 0 || savetime < timestamp || (timestamp < 0 && savetime > 0))
		{
			final long now = System.nanoTime();
			if (Math.abs(now - savetime) < StorageQueue.DELAY)
			{
				return StorageQueue.RequestState.REQUEUE;
			}
			else
			{
				savetime = System.nanoTime();
				return StorageQueue.RequestState.SCHEDULE;
			}
		}
		else
		{
			return StorageQueue.RequestState.FINISHED;
		}
	}

	Runnable getFileWriter()
	{
		return writer;
	}


	private class StorageObjectDataWriter extends AbstractDelayedYamlFileWriter
	{
		public StorageObjectDataWriter()
		{
			super(ess);
		}

		@Override
		public File getFile()
		{
			return file;
		}

		@Override
		public StorageObject getObject()
		{
			return getData();
		}

		@Override
		public void onFinish()
		{
			finishWrite();
		}
	}


	private class StorageObjectDataReader extends AbstractDelayedYamlFileReader<T>
	{
		public StorageObjectDataReader()
		{
			super(ess, clazz);
		}

		@Override
		public File onStart()
		{
			return file;
		}

		@Override
		public void onSuccess(final T object)
		{
			if (object != null)
			{
				data = object;
			}
			loaded.set(true);
			finishRead();
		}

		@Override
		public void onException(final Exception exception)
		{
			if (data == null)
			{
				try
				{
					data = clazz.newInstance();
				}
				catch (Exception ex)
				{
					Bukkit.getLogger().log(Level.SEVERE, ex.getMessage(), ex);
				}
			}
			loaded.set(true);
			if (exception instanceof FileNotFoundException)
			{
				fillWithDefaults();
				writer.schedule();
			}
		}
	}
}
