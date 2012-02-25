package com.earth2me.essentials.components.settings;

import com.earth2me.essentials.api.IContext;
import com.earth2me.essentials.api.IEssentials;
import com.earth2me.essentials.api.ISettingsComponent;
import com.earth2me.essentials.storage.StorageComponent;
import java.io.File;
import java.util.concurrent.atomic.AtomicBoolean;


public class SettingsComponent extends StorageComponent<Settings, IEssentials> implements ISettingsComponent
{
	private final transient AtomicBoolean debug = new AtomicBoolean(false);

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
			debug.set(getData().getGeneral().isDebug());
		}
		finally
		{
			unlock();
		}
	}

	@Override
	public File getStorageFile()
	{
		return new File(getPlugin().getDataFolder(), "settings.yml");
	}

	@Override
	public String getLocale()
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
		return debug.get();
	}

	@Override
	public void setDebug(final boolean set)
	{
		acquireWriteLock();
		try
		{
			getData().getGeneral().setDebug(set);
			debug.set(set);
		}
		finally
		{
			unlock();
		}
	}
}
