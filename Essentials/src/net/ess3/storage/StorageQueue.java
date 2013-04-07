package net.ess3.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import net.ess3.api.IPlugin;


public class StorageQueue implements Runnable
{
	private DelayQueue<WriteRequest> queue = new DelayQueue<WriteRequest>();
	public final static long DELAY = TimeUnit.NANOSECONDS.convert(1, TimeUnit.SECONDS);
	private final AtomicBoolean enabled = new AtomicBoolean(false);
	private final IPlugin plugin;

	public StorageQueue(IPlugin plugin)
	{
		this.plugin = plugin;
	}

	@Override
	public void run()
	{
		if (enabled.get() || !queue.isEmpty())
		{
			work();
		}
	}

	private void work()
	{
		final List<WriteRequest> requests = new ArrayList<WriteRequest>();
		queue.drainTo(requests);
		for (WriteRequest request : requests)
		{
			final RequestState state = request.getRequestState();
			if (state == RequestState.REQUEUE)
			{
				queue.add(request);
				continue;
			}
			else if (state == RequestState.SCHEDULE)
			{
				plugin.runTaskAsynchronously(request.getRunnable());
			}
		}
	}

	public void queue(AsyncStorageObjectHolder objectHolder)
	{
		if (!enabled.get())
		{
			plugin.getLogger().log(
					Level.SEVERE,
					"File " + objectHolder.toString() + " is queued for saving, while the queue is disabled. It's possible that it will not be saved!",
					new RuntimeException());
		}
		queue.add(new WriteRequest(objectHolder));
	}

	private void startThread()
	{
		plugin.runTaskTimerAsynchronously(this, 5, 5);
	}

	public void setEnabled(boolean enabled)
	{
		if (this.enabled.getAndSet(enabled) != enabled)
		{
			if (enabled)
			{
				startThread();
			}
			else
			{
				while (queue.size() > 0)
				{
					work();
					try
					{
						Thread.sleep(50);
					}
					catch (InterruptedException ex)
					{
					}
				}
			}
		}

	}

	public int getQueueSize()
	{
		return queue.size();
	}


	private static class WriteRequest implements Delayed
	{
		private final AsyncStorageObjectHolder objectHolder;
		private final long timestamp;

		public WriteRequest(AsyncStorageObjectHolder objectHolder)
		{
			this.objectHolder = objectHolder;
			this.timestamp = System.nanoTime();
		}

		@Override
		public long getDelay(final TimeUnit tu)
		{
			if (tu == TimeUnit.NANOSECONDS)
			{
				final long now = System.nanoTime();
				return DELAY - Math.abs(now - timestamp);
			}
			else
			{
				return tu.convert(getDelay(TimeUnit.NANOSECONDS), TimeUnit.NANOSECONDS);
			}
		}

		@Override
		public int compareTo(final Delayed t)
		{
			final long a = getDelay(TimeUnit.NANOSECONDS);
			final long b = t.getDelay(TimeUnit.NANOSECONDS);
			return a < b ? -1 : a == b ? 0 : 1;
		}

		public RequestState getRequestState()
		{
			return objectHolder.getRequestState(timestamp);
		}

		public Runnable getRunnable()
		{
			return objectHolder.getFileWriter();
		}
	}


	public enum RequestState
	{
		REQUEUE,
		SCHEDULE,
		FINISHED
	}
}
