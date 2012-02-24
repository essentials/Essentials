package com.earth2me.essentials.api;

import com.earth2me.essentials.components.IComponent;
import com.earth2me.essentials.components.settings.Settings;
import com.earth2me.essentials.storage.IStorageObjectHolder;


public interface ISettingsComponent extends IComponent, IStorageObjectHolder<Settings>
{
	public String getLocale();

	public boolean isDebug();

	public void setDebug(boolean b);
}
