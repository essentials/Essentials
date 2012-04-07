package com.earth2me.essentials.user;

import com.earth2me.essentials.utils.Util;
import com.earth2me.essentials.api.IEssentials;
import com.earth2me.essentials.api.ISettings;
import com.earth2me.essentials.api.InvalidNameException;
import com.earth2me.essentials.api.server.Player;
import com.earth2me.essentials.api.server.Location;
import com.earth2me.essentials.storage.AsyncStorageObjectHolder;
import com.earth2me.essentials.storage.IStorageObjectHolder;
import com.earth2me.essentials.storage.StoredLocation.WorldNotLoadedException;
import java.io.File;
import java.io.IOException;
import java.util.*;
import lombok.Cleanup;
import lombok.Delegate;


public abstract class UserBase extends AsyncStorageObjectHolder<UserData> implements Player, IStorageObjectHolder<UserData>
{
	@Delegate
	protected Player base;

	public UserBase(final Player base, final IEssentials ess)
	{
		super(ess, UserData.class);
		this.base = base;
		onReload();
	}

	public final Player getBase()
	{
		return base;
	}

	public final Player setBase(final Player base)
	{
		return this.base = base;
	}

	public void update(final Player base)
	{
		setBase(base);
	}



	public void dispose()
	{
		this.base = null;
	}

	@Override
	public File getStorageFile() throws IOException
	{
		try
		{
			return ess.getUserMap().getUserFile(getName());
		}
		catch (InvalidNameException ex)
		{
			throw new IOException(ex.getMessage(), ex);
		}
	}

	public long getTimestamp(final UserData.TimestampType name)
	{
		acquireReadLock();
		try
		{
			if (getData().getTimestamps() == null)
			{
				return 0;
			}
			Long ts = getData().getTimestamps().get(name);
			return ts == null ? 0 : ts;
		}
		finally
		{
			unlock();
		}
	}

	public void setTimestamp(final UserData.TimestampType name, final long value)
	{
		acquireWriteLock();
		try
		{
			if (getData().getTimestamps() == null)
			{
				getData().setTimestamps(new HashMap<UserData.TimestampType, Long>());
			}
			getData().getTimestamps().put(name, value);
		}
		finally
		{
			unlock();
		}
	}

	public void setMoney(final double value)
	{
		acquireWriteLock();
		try
		{
			@Cleanup
			final ISettings settings = ess.getSettings();
			settings.acquireReadLock();
			if (Math.abs(value) > settings.getData().getEconomy().getMaxMoney())
			{
				getData().setMoney(value < 0 ? -settings.getData().getEconomy().getMaxMoney() : settings.getData().getEconomy().getMaxMoney());
			}
			else
			{
				getData().setMoney(value);
			}
		}
		finally
		{
			unlock();
		}
	}

	public double getMoney()
	{
		acquireReadLock();
		try
		{
			Double money = getData().getMoney();
			@Cleanup
			final ISettings settings = ess.getSettings();
			settings.acquireReadLock();
			if (money == null)
			{
				money = (double)settings.getData().getEconomy().getStartingBalance();
			}
			if (Math.abs(money) > settings.getData().getEconomy().getMaxMoney())
			{
				money = money < 0 ? -settings.getData().getEconomy().getMaxMoney() : settings.getData().getEconomy().getMaxMoney();
			}
			return money;
		}
		finally
		{
			unlock();
		}
	}

	public void setHome(String name, Location loc)
	{
		acquireWriteLock();
		try
		{
			Map<String, com.earth2me.essentials.storage.StoredLocation> homes = getData().getHomes();
			if (homes == null)
			{
				homes = new HashMap<String, com.earth2me.essentials.storage.StoredLocation>();
				getData().setHomes(homes);
			}
			homes.put(Util.sanitizeKey(name), new com.earth2me.essentials.storage.StoredLocation(loc));
		}
		finally
		{
			unlock();
		}
	}

	public boolean toggleAfk()
	{
		acquireWriteLock();
		try
		{
			boolean ret = !getData().isAfk();
			getData().setAfk(ret);
			return ret;
		}
		finally
		{
			unlock();
		}
	}

	public boolean toggleGodmode()
	{
		acquireWriteLock();
		try
		{
			boolean ret = !getData().isGodmode();
			getData().setGodmode(ret);
			return ret;
		}
		finally
		{
			unlock();
		}
	}

	public boolean toggleMuted()
	{
		acquireWriteLock();
		try
		{
			boolean ret = !getData().isMuted();
			getData().setMuted(ret);
			return ret;
		}
		finally
		{
			unlock();
		}
	}

	public boolean toggleSocialSpy()
	{
		acquireWriteLock();
		try
		{
			boolean ret = !getData().isSocialspy();
			getData().setSocialspy(ret);
			return ret;
		}
		finally
		{
			unlock();
		}
	}

	public boolean toggleTeleportEnabled()
	{
		acquireWriteLock();
		try
		{
			boolean ret = !getData().isTeleportEnabled();
			getData().setTeleportEnabled(ret);
			return ret;
		}
		finally
		{
			unlock();
		}
	}

	public boolean isIgnoringPlayer(final String name)
	{
		acquireReadLock();
		try
		{
			return getData().getIgnore() == null ? false : getData().getIgnore().contains(name.toLowerCase(Locale.ENGLISH));
		}
		finally
		{
			unlock();
		}
	}

	public void setIgnoredPlayer(final String name, final boolean set)
	{
		acquireWriteLock();
		try
		{
			if (getData().getIgnore() == null)
			{
				getData().setIgnore(new HashSet<String>());
			}
			if (set)
			{
				getData().getIgnore().add(name.toLowerCase(Locale.ENGLISH));
			}
			else
			{
				getData().getIgnore().remove(name.toLowerCase(Locale.ENGLISH));
			}
		}
		finally
		{
			unlock();
		}
	}

	public void addMail(String string)
	{
		acquireWriteLock();
		try
		{
			if (getData().getMails() == null)
			{
				getData().setMails(new ArrayList<String>());
			}
			getData().getMails().add(string);
		}
		finally
		{
			unlock();
		}
	}

	public List<String> getMails()
	{
		acquireReadLock();
		try
		{
			if (getData().getMails() == null)
			{
				return Collections.emptyList();
			}
			else
			{
				return new ArrayList<String>(getData().getMails());
			}
		}
		finally
		{
			unlock();
		}
	}

	public Location getHome(Location loc)
	{

		acquireReadLock();
		try
		{
			if (getData().getHomes() == null)
			{
				return null;
			}
			ArrayList<Location> worldHomes = new ArrayList<Location>();
			for (com.earth2me.essentials.storage.StoredLocation location : getData().getHomes().values())
			{
				if (location.getWorldName().equals(loc.getWorld().getName()))
				{
					try
					{
						worldHomes.add(location.getStoredLocation());
					}
					catch (WorldNotLoadedException ex)
					{
						continue;
					}
				}
			}
			if (worldHomes.isEmpty())
			{
				return null;
			}
			if (worldHomes.size() == 1)
			{
				return worldHomes.get(0);
			}
			double distance = Double.MAX_VALUE;
			Location target = null;
			for (Location location : worldHomes)
			{
				final double d = loc.distanceSquared(location);
				if (d < distance)
				{
					target = location;
					distance = d;
				}
			}
			return target;
		}
		finally
		{
			unlock();
		}
	}
}
