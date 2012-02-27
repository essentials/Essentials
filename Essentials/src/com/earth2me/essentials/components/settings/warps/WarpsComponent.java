package com.earth2me.essentials.components.settings.warps;

import com.earth2me.essentials.api.IContext;
import com.earth2me.essentials.api.IEssentials;
import com.earth2me.essentials.storage.StorageComponent;


public final class WarpsComponent extends StorageComponent<Warps, IEssentials> implements IWarpsComponent
{
	public WarpsComponent(IContext context, IEssentials plugin)
	{
		super(context, Warps.class, plugin);
	}

	@Override
	public String getContainerId()
	{
		return "settings.warps";
	}

	@Override
	public Warp getWarp(final String name)
	{
		acquireReadLock();
		try
		{
			return getData().getWarps().get(name);
		}
		finally
		{
			unlock();
		}
	}

	@Override
	public boolean containsWarp(final String name)
	{
		acquireReadLock();
		try
		{
			return getData().getWarps().containsKey(name);
		}
		finally
		{
			unlock();
		}
	}

	@Override
	public void setWarp(String name, Warp warp)
	{
		acquireWriteLock();
		try
		{
			getData().getWarps().put(name, warp);
		}
		finally
		{
			unlock();
		}
	}

	@Override
	public Warp removeWarp(String name)
	{
		acquireWriteLock();
		try
		{
			return getData().getWarps().remove(name);
		}
		finally
		{
			unlock();
		}
	}
}
