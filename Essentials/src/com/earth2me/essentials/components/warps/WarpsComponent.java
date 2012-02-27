package com.earth2me.essentials.components.warps;

import com.earth2me.essentials.components.settings.warps.IWarpComponent;
import static com.earth2me.essentials.components.i18n.I18nComponent._;
import com.earth2me.essentials.api.IContext;
import com.earth2me.essentials.api.InvalidNameException;
import com.earth2me.essentials.components.commands.WarpNotFoundException;
import com.earth2me.essentials.components.settings.warps.WarpComponent;
import com.earth2me.essentials.storage.LocationData;
import com.earth2me.essentials.storage.StorageObjectMap;
import java.io.File;
import java.util.*;
import org.bukkit.Location;


public class WarpsComponent extends StorageObjectMap<IWarpComponent> implements IWarpComponent
{
	public WarpsComponent(IContext context)
	{
		super(context, "warps");
	}

	@Override
	public String getTypeId()
	{
		return "WarpsComponent";
	}

	@Override
	public void initialize()
	{
	}

	@Override
	public void onEnable()
	{
		reload();
	}

	@Override
	public void close()
	{
		cache.cleanUp();
	}

	@Override
	public boolean isEmpty()
	{
		return getKeySize() == 0;
	}

	@Override
	public Collection<String> getList()
	{
		final List<String> names = new ArrayList<String>();
		for (String key : getAllKeys())
		{
			IWarpComponent warp = getObject(key);
			if (warp == null)
			{
				continue;
			}
			warp.acquireReadLock();
			try
			{
				names.add(warp.getData().getName());
			}
			finally
			{
				warp.unlock();
			}
		}
		Collections.sort(names, String.CASE_INSENSITIVE_ORDER);
		return names;
	}

	@Override
	public Location getWarp(final String name) throws Exception
	{
		IWarpComponent warp = getObject(name);
		if (warp == null)
		{
			throw new WarpNotFoundException(_("warpNotExist"));
		}
		warp.acquireReadLock();
		try
		{
			return warp.getData().getLocation().getBukkitLocation();
		}
		finally
		{
			warp.unlock();
		}
	}

	@Override
	public void setWarp(final String name, final Location loc) throws Exception
	{
		setWarp(name, new LocationData(loc));
	}

	public void setWarp(final String name, final LocationData loc) throws Exception
	{
		IWarpComponent warp = getObject(name);
		if (warp == null)
		{
			warp = new WarpComponent(name, context, context.getEssentials());
		}
		warp.acquireWriteLock();
		try
		{
			warp.getData().setLocation(loc);
		}
		finally
		{
			warp.unlock();
		}
	}

	@Override
	public void removeWarp(final String name) throws Exception
	{
		removeObject(name);
	}

	@Override
	public File getWarpFile(String name) throws InvalidNameException
	{
		return getStorageFile(name);
	}

	@Override
	public IWarpComponent load(String name) throws Exception
	{
		final IWarpComponent warp = new WarpComponent(name, context, context.getEssentials());
		warp.reload();
		return warp;
	}


	private static class StringIgnoreCase
	{
		private final String string;

		public StringIgnoreCase(String string)
		{
			this.string = string;
		}

		@Override
		public int hashCode()
		{
			return getString().toLowerCase(Locale.ENGLISH).hashCode();
		}

		@Override
		public boolean equals(Object o)
		{
			if (o instanceof StringIgnoreCase)
			{
				return getString().equalsIgnoreCase(((StringIgnoreCase)o).getString());
			}
			return false;
		}

		public String getString()
		{
			return string;
		}
	}
}
