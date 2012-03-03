package com.earth2me.essentials.storage;

import com.earth2me.essentials.api.IContext;
import com.earth2me.essentials.components.Component;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import lombok.Setter;
import org.bukkit.Bukkit;


public abstract class StorageComponent<T extends IStorageObject> extends Component implements IStorageComponent<T>
{
	// Explicitly defined getter.
	@Setter
	private transient T data;
	private final transient ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
	private final transient Class<T> clazz;
	private final transient StorageObjectDataWriter writer;
	private final transient StorageObjectDataReader reader;
	private final transient AtomicBoolean loaded = new AtomicBoolean(false);
	// Explicitly defined getter.
	@Setter
	private transient File storageFile;

	protected StorageComponent(final IContext context, final Class<T> clazz)
	{
		super(context);

		this.clazz = clazz;

		writer = new StorageObjectDataWriter();
		reader = new StorageObjectDataReader();

		try
		{
			data = clazz.newInstance();
		}
		catch (Throwable ex)
		{
			Bukkit.getLogger().log(Level.SEVERE, ex.getMessage(), ex);
		}
	}

	@Override
	public void onEnable()
	{
		reload();
	}

	/**
	 * Warning: If you access this method, you have to acquire a read or write lock first
	 *
	 *
	 * @return Object storing all the data
	 */
	@Override
	public final T getData()
	{
		if (!loaded.get())
		{
			reader.schedule(true);
		}
		return data;
	}

	@Override
	public final void acquireReadLock()
	{
		rwl.readLock().lock();
	}

	@Override
	public final void acquireWriteLock()
	{
		while (rwl.getReadHoldCount() > 0)
		{
			rwl.readLock().unlock();
		}
		rwl.writeLock().lock();
		rwl.readLock().lock();
	}

	@Override
	public final void unlock()
	{
		if (rwl.isWriteLockedByCurrentThread())
		{
			rwl.writeLock().unlock();
			writer.schedule();
		}
		while (rwl.getReadHoldCount() > 0)
		{
			rwl.readLock().unlock();
		}
	}

	@Override
	public void reload()
	{
		reload(true);
		// Call super.reload in reload(boolean).
	}

	@Override
	public void reload(final boolean instant)
	{
		reader.schedule(instant);

		super.reload();
	}

	@Override
	public final File getStorageFile()
	{
		if (storageFile == null)
		{
			storageFile = new File(getContext().getEssentials().getDataFolder(), getContainerId() + ".yml");
		}
		return storageFile;
	}


	private class StorageObjectDataWriter extends AbstractDelayedYamlFileWriter
	{
		public StorageObjectDataWriter()
		{
			super(getContext());
		}

		@Override
		public File getFile() throws IOException
		{
			return getStorageFile();
		}

		@Override
		public IStorageObject getObject()
		{
			acquireReadLock();
			return getData();
		}

		@Override
		public void onFinish()
		{
			unlock();
		}
	}


	private class StorageObjectDataReader extends AbstractDelayedYamlFileReader<T>
	{
		public StorageObjectDataReader()
		{
			super(getContext(), clazz);
		}

		@Override
		public File onStart() throws IOException
		{
			final File file = getStorageFile();
			while (rwl.getReadHoldCount() > 0)
			{
				rwl.readLock().unlock();
			}
			rwl.writeLock().lock();
			return file;
		}

		@Override
		public void onSuccess(final T object)
		{
			if (object != null)
			{
				data = object;
			}
			rwl.writeLock().unlock();
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
			rwl.writeLock().unlock();
			loaded.set(true);
			if (exception instanceof FileNotFoundException)
			{
				writer.schedule();
			}
		}
	}
}
