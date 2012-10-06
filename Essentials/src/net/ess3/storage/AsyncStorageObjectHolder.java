package net.ess3.storage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
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

	public AsyncStorageObjectHolder(final IEssentials ess, final Class<T> clazz)
	{
		this.ess = ess;
		this.clazz = clazz;
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
		writer.schedule();
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
	
	public abstract void finishRead();
	
	public abstract void finishWrite();
	
	public abstract File getStorageFile() throws IOException;


	private class StorageObjectDataWriter extends AbstractDelayedYamlFileWriter
	{
		public StorageObjectDataWriter()
		{
			super(ess);
		}

		@Override
		public File getFile() throws IOException
		{
			return getStorageFile();
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
		public File onStart() throws IOException
		{
			return getStorageFile();
		}

		@Override
		public void onSuccess(final T object)
		{
			if (object != null)
			{
				data = object;
			}
			loaded.set(true);
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
				writer.schedule();
			}
		}
	}
}
