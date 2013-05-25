package net.ess3.api;

import net.ess3.settings.Settings;
import net.ess3.storage.IStorageObjectHolder;


public interface ISettings extends IStorageObjectHolder<Settings>
{
	/**
	 *
	 * @return
	 */
	String getLocale();

	/**
	 *
	 * @return
	 */
	boolean isDebug();

	/**
	 *
	 * @param b **TODO: rename this, "b" is a terrible name**
	 */
	void setDebug(boolean b);
}
