package net.ess3.storage;


public interface IStorageReader
{
	<T extends StorageObject> T load(final Class<? extends T> clazz) throws ObjectLoadException;
}
