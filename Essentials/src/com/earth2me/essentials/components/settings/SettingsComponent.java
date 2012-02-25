package com.earth2me.essentials.components.settings;

import com.earth2me.essentials.api.IContext;
import com.earth2me.essentials.api.IEssentials;
import com.earth2me.essentials.api.ISettingsComponent;
import com.earth2me.essentials.storage.StorageComponent;


public class SettingsComponent extends StorageComponent<Settings, IEssentials> implements ISettingsComponent
{
	/*
	 * volatiles are more efficient than Atomics for settings, since we know we can never write simultaneously, thanks
	 * to write locks. volatiles are actually going to be safer, since we're throwing around a value, not a reference:
	 * that is, we have control over when it will update out of context.
	 *
	 * volatile is going to ensure that all threads are updated immedately when one thread writes to the object, but it
	 * will not solve write conflicts, as it does not lock. Atomics lock, meaning that they are both read-safe and
	 * write-safe, while volatiles are only read-synchronized--not even blocking.
	 *
	 * The word "synchronized" is often misused in Java and C#: inconcurrent would be a much better word. volatiles are
	 * CONCURRENT yet synchronized (this obviously means concurrent writes have an indeterminate result, but does NOT
	 * mean they lock). Atomics are INCONCURRENT and synchronized--that is, they lock.
	 *
	 * Were you to omit the volatile keyword, there would be little difference: the primitive would still be concurrent.
	 * However, it would no longer be synchronized. This means that, if a thread were to read from it shortly after
	 * another thread were to write to it, the reading thread would likely see the old value, not the newly written
	 * value.
	 *
	 * tl;dr: If only one thread can write at a time, use volatile, not AtomicXxxxx.
	 */
	private transient volatile boolean debug;

	public SettingsComponent(final IContext context, final IEssentials plugin)
	{
		super(context, Settings.class, plugin);
	}

	@Override
	public final void reload()
	{
		super.reload();

		acquireReadLock();
		try
		{
			debug = getData().getGeneral().isDebug();
		}
		finally
		{
			unlock();
		}
	}

	@Override
	public String getContainerId()
	{
		return "general";
	}

	@Override
	public String getLocaleSafe()
	{
		acquireReadLock();
		try
		{
			return getData().getGeneral().getLocale();
		}
		finally
		{
			unlock();
		}
	}

	@Override
	public boolean isDebug()
	{
		return debug;
	}

	@Override
	public void setDebug(final boolean debug)
	{
		acquireWriteLock();
		try
		{
			getData().getGeneral().setDebug(debug);
			this.debug = debug;
		}
		finally
		{
			unlock();
		}
	}
}
