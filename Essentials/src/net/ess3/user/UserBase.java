package net.ess3.user;

import java.io.File;
import java.io.IOException;
import java.util.*;
import lombok.Cleanup;
import lombok.Delegate;
import net.ess3.api.IEssentials;
import net.ess3.api.ISettings;
import net.ess3.api.IUser;
import net.ess3.api.InvalidNameException;
import net.ess3.permissions.Permissions;
import net.ess3.storage.AsyncStorageObjectHolder;
import net.ess3.storage.IStorageObjectHolder;
import net.ess3.storage.StoredLocation.WorldNotLoadedException;
import net.ess3.utils.Util;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;


public abstract class UserBase extends AsyncStorageObjectHolder<UserData> implements OfflinePlayer, CommandSender, IStorageObjectHolder<UserData>
{
	@Delegate
	protected final OfflinePlayer offlinePlayer;

	public UserBase(final OfflinePlayer base, final IEssentials ess)
	{
		super(ess, UserData.class);
		this.offlinePlayer = base;
		onReload();
	}
	
	@Override
	public void sendMessage(String string)
	{
		Player player = offlinePlayer.getPlayer();
		if (player != null) {
			player.sendMessage(string);
		}
	}

	@Override
	public void sendMessage(String[] strings)
	{
		Player player = offlinePlayer.getPlayer();
		if (player != null) {
			player.sendMessage(strings);
		}
	}

	@Override
	public Server getServer()
	{
		return ess.getServer();
	}

	@Override
	public boolean isPermissionSet(String string)
	{
		Player player = offlinePlayer.getPlayer();
		if (player != null) {
			return player.isPermissionSet(string);
		} else {
			return false;
		}
	}

	@Override
	public boolean isPermissionSet(Permission prmsn)
	{
		Player player = offlinePlayer.getPlayer();
		if (player != null) {
			return player.isPermissionSet(prmsn);
		} else {
			return false;
		}
	}

	@Override
	public boolean hasPermission(String string)
	{
		Player player = offlinePlayer.getPlayer();
		if (player != null) {
			return player.hasPermission(string);
		} else {
			return false;
		}
	}

	@Override
	public boolean hasPermission(Permission prmsn)
	{
		Player player = offlinePlayer.getPlayer();
		if (player != null) {
			return player.hasPermission(prmsn);
		} else {
			return false;
		}
	}

	@Override
	public PermissionAttachment addAttachment(Plugin plugin, String string, boolean bln)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public PermissionAttachment addAttachment(Plugin plugin)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public PermissionAttachment addAttachment(Plugin plugin, String string, boolean bln, int i)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public PermissionAttachment addAttachment(Plugin plugin, int i)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void removeAttachment(PermissionAttachment pa)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void recalculatePermissions()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public Set<PermissionAttachmentInfo> getEffectivePermissions()
	{
		throw new UnsupportedOperationException("Not supported yet.");
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
			return getData().getTimestamp(name);
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
			getData().setTimestamp(name, value);
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
			getData().addHome(Util.sanitizeKey(name), loc);
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

	public void setGodModeEnabled(boolean set)
	{
		acquireWriteLock();
		try
		{
			getData().setGodmode(set);
		}
		finally
		{
			unlock();
		}
	}

	public void setMuted(boolean mute)
	{
		acquireWriteLock();
		try
		{
			getData().setMuted(mute);
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

	public boolean isIgnoringPlayer(final IUser user)
	{
		acquireReadLock();
		try
		{
			return getData().getIgnore() == null ? false : getData().getIgnore().contains(user.getName().toLowerCase(Locale.ENGLISH)) && Permissions.CHAT_IGNORE_EXEMPT.isAuthorized(user);
		}
		finally
		{
			unlock();
		}
	}

	public void setIgnoredPlayer(final IUser user, final boolean set)
	{
		acquireWriteLock();
		try
		{
			getData().setIgnore(user.getName().toLowerCase(Locale.ENGLISH), set);
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
			getData().addMail(string);
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
			return getData().getMails();
		}
		finally
		{
			unlock();
		}
	}

	public Location getHome(String name) throws Exception
	{
		acquireReadLock();
		try
		{
			if (getData().getHomes() == null)
			{
				return null;
			}
			try
			{
				return getData().getHomes().get(Util.sanitizeKey(name)).getStoredLocation();
			}
			catch (WorldNotLoadedException ex)
			{
				return null;
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
			for (net.ess3.storage.StoredLocation location : getData().getHomes().values())
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
	
	public Set<String> getHomes()
	{
		acquireReadLock();
		try
		{	
			return getData().getHomes().keySet();
		}
		finally
		{
			unlock();
		}
	}
}
