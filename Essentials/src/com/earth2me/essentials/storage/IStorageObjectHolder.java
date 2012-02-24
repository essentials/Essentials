package com.earth2me.essentials.storage;

import com.earth2me.essentials.api.IReloadable;


public interface IStorageObjectHolder<T extends IStorageObject> extends IReloadable
{
	T getData();

	void acquireReadLock();

	void acquireWriteLock();

	void close();

	void unlock();
}
