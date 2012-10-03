package net.ess3.protect;

import org.bukkit.plugin.Plugin;


public interface IProtect extends Plugin
{
	EssentialsConnect getEssentialsConnect();

	ProtectHolder getSettings();

	void setSettings(ProtectHolder settings);
}
