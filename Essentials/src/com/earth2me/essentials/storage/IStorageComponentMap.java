package com.earth2me.essentials.storage;

import com.earth2me.essentials.api.InvalidNameException;
import com.earth2me.essentials.components.IComponent;
import java.io.File;
import java.util.Set;


public interface IStorageComponentMap<I> extends IComponent
{
	boolean objectExists(final String name);

	I getObject(final String name);

	boolean removeObject(final String name) throws InvalidNameException;

	Set<String> getAllKeys();

	int getKeySize();

	File getStorageFile(final String name) throws InvalidNameException;
}
