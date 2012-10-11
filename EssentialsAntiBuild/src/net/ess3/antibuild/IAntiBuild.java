package net.ess3.antibuild;


import org.bukkit.plugin.Plugin;

public interface IAntiBuild extends Plugin
{
	EssentialsConnect getEssentialsConnect();
	
	AntiBuildHolder getSettings();

	void setSettings(AntiBuildHolder settings);
}
