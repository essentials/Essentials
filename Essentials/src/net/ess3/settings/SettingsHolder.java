package net.ess3.settings;

import java.io.File;
import net.ess3.api.IEssentials;
import net.ess3.api.ISettings;
import net.ess3.storage.AsyncStorageObjectHolder;


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
	private transient volatile boolean debug = false;

	public SettingsHolder(final IEssentials ess)
	{
		super(ess, Settings.class);
		onReload();
	}

	@Override
	public final void onReload()
	{
		super.onReload();

		debug = getData().getGeneral().isDebug();
	}

	@Override
	public File getStorageFile()
	{
		return new File(ess.getPlugin().getDataFolder(), "settings.yml");
	}

	@Override
	public String getLocale()
	{
		return getData().getGeneral().getLocale();
	}

	@Override
	public boolean isDebug()
	{
		return debug;
	}

	@Override
	public void setDebug(final boolean set)
	{
		debug = set;
		getData().getGeneral().setDebug(set);
		queueSave();
	}
}
