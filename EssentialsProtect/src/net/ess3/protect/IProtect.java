package net.ess3.protect;

import org.bukkit.plugin.Plugin;


public interface IProtect extends Plugin
{
	/**
	 *
	 * @return
	 */
	EssentialsConnect getEssentialsConnect();

	/**
	 *
	 * @return
	 */
	ProtectHolder getSettings();

	/**
	 *
	 * @param settings
	 */
	void setSettings(ProtectHolder settings);
}
