package net.ess3.api;

import net.ess3.settings.Settings;
import net.ess3.storage.IStorageObjectHolder;


public interface ISettings extends IStorageObjectHolder<Settings>
{
	String getLocale();

	boolean isDebug();

	void setDebug(boolean b);
}
