package net.ess3.protect;

import org.bukkit.plugin.Plugin;


public interface IProtect extends Plugin
{
//	boolean checkProtectionItems(final ProtectConfig list, final int id);
//	boolean getSettingBool(final ProtectConfig protectConfig);
//	String getSettingString(final ProtectConfig protectConfig);


	EssentialsConnect getEssentialsConnect();

//	Map<ProtectConfig, Boolean> getSettingsBoolean();
//	Map<ProtectConfig, String> getSettingsString();
//	Map<ProtectConfig, List<Integer>> getSettingsList();
	ProtectHolder getSettings();

	void setSettings(ProtectHolder settings);
}
