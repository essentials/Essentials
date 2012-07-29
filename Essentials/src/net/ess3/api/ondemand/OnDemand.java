package net.ess3.api.ondemand;

import java.lang.ref.WeakReference;


public abstract class OnDemand<T>
{
	protected WeakReference<T> reference = null;

	public final T get()
	{
		T obj = reference == null ? null : reference.get();
		if (obj == null)
		{
			obj = getNew();
			reference = new WeakReference<T>(obj);
		}
		return obj;
	}

	protected abstract T getNew();
}
