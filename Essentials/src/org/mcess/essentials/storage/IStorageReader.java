package org.mcess.essentials.storage;


public interface IStorageReader
{
	 <T extends StorageObject> T load(final Class<? extends T> clazz) throws ObjectLoadException;
}
