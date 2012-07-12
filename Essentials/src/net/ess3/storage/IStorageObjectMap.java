package net.ess3.storage;

import java.io.File;
import java.util.Set;
import net.ess3.api.IReload;
import net.ess3.api.InvalidNameException;


interface IStorageObjectMap<I> extends IReload
{
	boolean objectExists(final String name);

	I getObject(final String name);

	void removeObject(final String name) throws InvalidNameException;

	Set<String> getAllKeys();

	int getKeySize();

	File getStorageFile(final String name) throws InvalidNameException;
}
