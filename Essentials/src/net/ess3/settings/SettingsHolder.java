package net.ess3.settings;

import net.ess3.api.IEssentials;
import net.ess3.api.ISettings;
import net.ess3.storage.AsyncStorageObjectHolder;
import java.io.File;
import java.util.concurrent.atomic.AtomicBoolean;


public class SettingsHolder extends AsyncStorageObjectHolder<Settings> implements ISettings
{

	@Override
	public void finishRead()
	{
		
	}

	@Override
	public void finishWrite()
	{
		
	}
	private final transient AtomicBoolean debug = new AtomicBoolean(false);
	public SettingsHolder(final IEssentials ess)
	{
		super(ess, Settings.class);
		onReload();
	}

	@Override
	public final void onReload()
	{
		super.onReload();
		acquireReadLock();
		try {
			debug.set(getData().getGeneral().isDebug());
		} finally {
			unlock();
		}
	}

	@Override
	public File getStorageFile()
	{
		return new File(ess.getDataFolder(), "settings.yml");
	}

	@Override
	public String getLocale()
	{
		acquireReadLock();
		try {
			return getData().getGeneral().getLocale();
		} finally {
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
		try {
			getData().getGeneral().setDebug(set);
		} finally {
			unlock();
		}
	}
}
