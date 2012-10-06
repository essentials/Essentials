package net.ess3.storage;

import net.ess3.api.IReload;


public interface IStorageObjectHolder<T extends StorageObject> extends IReload
{
	T getData();

	//void acquireReadLock();

	//void acquireWriteLock();

	void queueSave();

	//void unlock();
}
