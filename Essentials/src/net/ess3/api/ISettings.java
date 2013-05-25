package net.ess3.api;

import net.ess3.settings.Settings;
import net.ess3.storage.IStorageObjectHolder;


public interface ISettings extends IStorageObjectHolder<Settings>
{
	/**
	 * Used to get the locale string
	 *
	 * @return the current locale string (i.e. en_US)
	 */
	String getLocale();

	/**
	 * Used to check if debug mode is enabled
	 *
	 * @return true if debug is enabled
	 */
	boolean isDebug();

	/**
	 * Used to set debug mode
	 *
	 * @param set
	 */
	void setDebug(boolean set);
}
