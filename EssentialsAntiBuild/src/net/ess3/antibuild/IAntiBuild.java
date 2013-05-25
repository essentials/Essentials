package net.ess3.antibuild;

import org.bukkit.plugin.Plugin;


public interface IAntiBuild extends Plugin
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
	AntiBuildHolder getSettings();

	/**
	 *
	 * @param settings
	 */
	void setSettings(AntiBuildHolder settings);
}
