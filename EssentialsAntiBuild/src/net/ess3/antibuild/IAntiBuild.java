package net.ess3.antibuild;


import org.bukkit.plugin.Plugin;

public interface IAntiBuild extends Plugin
{
	boolean checkProtectionItems(final int id);	

	EssentialsConnect getEssentialsConnect();
	
	AntiBuildHolder getSettings();

	void setSettings(AntiBuildHolder settings);
}
