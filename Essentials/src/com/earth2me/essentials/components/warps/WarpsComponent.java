package com.earth2me.essentials.components.warps;

import com.earth2me.essentials.api.IContext;
import com.earth2me.essentials.api.InvalidNameException;
import com.earth2me.essentials.storage.LocationData;
import com.earth2me.essentials.storage.StorageComponentMap;
import java.util.Collections;
import java.util.Set;
import lombok.Cleanup;
import org.bukkit.Location;


public final class WarpsComponent extends StorageComponentMap<IWarpComponent> implements IWarpsComponent
{
	public WarpsComponent(final IContext context)
	{
		super(context, "warps");
	}

	@Override
	public Location getWarp(final String name)
	{
		@Cleanup
		IWarpComponent warp = getObject(name);
		warp.acquireReadLock();
		try
		{
			return warp.getData().getLocation().getBukkitLocation();
		}
		catch (LocationData.WorldNotLoadedException ex)
		{
			return null;
		}
	}

	@Override
	public boolean containsWarp(final String name)
	{
		return objectExists(name);
	}

	@Override
	public void setWarp(String name, Location warp)
	{
		setWarp(name, new LocationData(warp));
	}

	@Override
	public void setWarp(final String name, final LocationData loc)
	{
		IWarpComponent warp = getObject(name);
		if (warp == null)
		{
			warp = new WarpComponent(context, context.getEssentials());
		}
		warp.acquireWriteLock();
		try
		{
			warp.getData().setName(name);
			warp.getData().setLocation(loc);
		}
		finally
		{
			warp.unlock();
		}
	}
	@Override
	public boolean removeWarp(String name)
	{
		try
		{
			return removeObject(name);
		}
		catch (InvalidNameException ex)
		{
			return false;
		}
	}

	@Override
	public Set<String> getList()
	{
		return Collections.unmodifiableSet(getAllKeys());
	}

	@Override
	public IWarpComponent load(String name) throws Exception
	{
		final IWarpComponent warp = new WarpComponent(context, context.getEssentials());
		warp.reload();
		return warp;
	}
}
