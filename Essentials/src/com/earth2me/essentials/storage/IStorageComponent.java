package com.earth2me.essentials.storage;

import java.io.File;


public interface IStorageComponent<T extends IStorageObject> extends IStorageObjectHolder<T>
{
	String getContainerId();

	void setStorageFile(final File storageFile);

	File getStorageFile();

	void reload(final boolean instant);
}
