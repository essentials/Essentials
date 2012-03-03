package com.earth2me.essentials.storage;

import com.earth2me.essentials.components.IComponent;
import java.io.File;


public interface IStorageComponent<T extends IStorageObject> extends IComponent, IStorageObjectHolder<T>
{
	String getContainerId();

	void setStorageFile(final File storageFile);

	File getStorageFile();

	void reload(final boolean instant);
}
