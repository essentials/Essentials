package com.earth2me.essentials.components.warps;

import static com.earth2me.essentials.components.i18n.I18nComponent._;
import com.earth2me.essentials.api.IContext;
import com.earth2me.essentials.api.InvalidNameException;
import com.earth2me.essentials.components.commands.WarpNotFoundException;
import com.earth2me.essentials.components.settings.WarpHolder;
import com.earth2me.essentials.storage.StorageObjectMap;
import java.io.File;
import java.util.*;
import org.bukkit.Location;


public class WarpsComponent extends StorageObjectMap<IWarp> implements IWarpsComponent
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
			IWarp warp = getObject(key);
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
		IWarp warp = getObject(name);
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
		setWarp(name, new com.earth2me.essentials.storage.LocationData(loc));
	}

	public void setWarp(final String name, final com.earth2me.essentials.storage.LocationData loc) throws Exception
	{
		IWarp warp = getObject(name);
		if (warp == null)
		{
			warp = new WarpHolder(name, context);
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
	public IWarp load(String name) throws Exception
	{
		final IWarp warp = new WarpHolder(name, context);
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
