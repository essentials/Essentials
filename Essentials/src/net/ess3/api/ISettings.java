package net.ess3.api;

import net.ess3.settings.Settings;
import net.ess3.storage.IStorageObjectHolder;


public interface ISettings extends IStorageObjectHolder<Settings>
{
	public String getLocale();

	public boolean isDebug();

	public void setDebug(boolean b);
}
