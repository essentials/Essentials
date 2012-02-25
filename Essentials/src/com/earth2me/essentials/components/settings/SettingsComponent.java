package com.earth2me.essentials.components.settings;

import com.earth2me.essentials.api.IContext;
import com.earth2me.essentials.api.ISettingsComponent;
import com.earth2me.essentials.storage.AsyncStorageObjectHolder;
import java.io.File;
import java.util.concurrent.atomic.AtomicBoolean;


public class SettingsComponent extends AsyncStorageObjectHolder<Settings> implements ISettingsComponent
{
	private final transient AtomicBoolean debug = new AtomicBoolean(false);

	public SettingsComponent(final IContext context)
	{
		super(context, Settings.class);
	}

	@Override
	public String getTypeId()
	{
		return "SettingsComponent";
	}

	@Override
	public void initialize()
	{
	}

	@Override
	public void onEnable()
	{
		reload();
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
		return new File(getContext().getDataFolder(), "settings.yml");
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
		debug.set(set);
		acquireWriteLock();
		try
		{
			getData().getGeneral().setDebug(set);
		}
		finally
		{
			unlock();
		}
	}
}
