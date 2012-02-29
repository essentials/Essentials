package com.earth2me.essentials.storage;

import org.bukkit.plugin.Plugin;


public interface ISubStorageComponent<T extends IStorageObject, U extends Plugin> extends IStorageComponent<T, U>
{
	void setContainerId(final String containerId);
}
