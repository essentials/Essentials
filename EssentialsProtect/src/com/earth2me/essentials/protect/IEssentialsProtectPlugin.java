package com.earth2me.essentials.protect;

import com.earth2me.essentials.api.IEssentialsPlugin;
import com.earth2me.essentials.protect.data.IProtectedBlock;


public interface IEssentialsProtectPlugin extends IEssentialsPlugin
{
//	boolean checkProtectionItems(final ProtectConfig list, final int id);
//	boolean getSettingBool(final ProtectConfig protectConfig);
//	String getSettingString(final ProtectConfig protectConfig);
	IProtectedBlock getStorage();

	void setStorage(IProtectedBlock pb);

//	Map<ProtectConfig, Boolean> getSettingsBoolean();
//	Map<ProtectConfig, String> getSettingsString();
//	Map<ProtectConfig, List<Integer>> getSettingsList();
	ProtectSettingsComponent getSettings();

	void setSettings(ProtectSettingsComponent settings);
}
