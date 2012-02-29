package com.earth2me.essentials.storage;

import java.io.File;
import org.bukkit.plugin.Plugin;


public interface IStorageComponent<T extends IStorageObject, U extends Plugin> extends IStorageObjectHolder<T>
{
	String getContainerId();

	U getPlugin();

	void setStorageFile(final File storageFile);

	File getStorageFile();

	void reload(final boolean instant);
}
