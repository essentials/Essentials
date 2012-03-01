package com.earth2me.essentials.components.settings;

import com.earth2me.essentials.api.IContext;
import com.earth2me.essentials.api.IEssentials;
import com.earth2me.essentials.api.ISettingsComponent;
import com.earth2me.essentials.storage.StorageComponent;


public class SettingsComponent extends StorageComponent<Settings, IEssentials> implements ISettingsComponent
{
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
