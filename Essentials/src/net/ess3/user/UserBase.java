package net.ess3.user;

import java.io.File;
import java.io.IOException;
import java.util.*;
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

	public UserBase(final OfflinePlayer base, final IEssentials ess) throws InvalidNameException
	{
		super(ess, UserData.class, ess.getUserMap().getUserFile(base.getName()));
		this.offlinePlayer = base;
		onReload();
	}

	@Override
	public void sendMessage(String string)
	{
		Player player = offlinePlayer.getPlayer();
		if (player != null)
		{
			player.sendMessage(string);
		}
	}

	@Override
	public void sendMessage(String[] strings)
	{
		Player player = offlinePlayer.getPlayer();
		if (player != null)
		{
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
		if (player != null)
		{
			return player.isPermissionSet(string);
		}
		else
		{
			return false;
		}
	}

	@Override
	public boolean isPermissionSet(Permission prmsn)
	{
		Player player = offlinePlayer.getPlayer();
		if (player != null)
		{
			return player.isPermissionSet(prmsn);
		}
		else
		{
			return false;
		}
	}

	@Override
	public boolean hasPermission(String string)
	{
		Player player = offlinePlayer.getPlayer();
		if (player != null)
		{
			return player.hasPermission(string);
		}
		else
		{
			return false;
		}
	}

	@Override
	public boolean hasPermission(Permission prmsn)
	{
		Player player = offlinePlayer.getPlayer();
		if (player != null)
		{
			return player.hasPermission(prmsn);
		}
		else
		{
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

	public long getTimestamp(final UserData.TimestampType name)
	{
		return getData().getTimestamp(name);
	}

	public void setTimestamp(final UserData.TimestampType name, final long value)
	{
		getData().setTimestamp(name, value);
		queueSave();
	}

	public void setMoney(final double value)
	{

		final ISettings settings = ess.getSettings();

		if (Math.abs(value) > settings.getData().getEconomy().getMaxMoney())
		{
			getData().setMoney(value < 0 ? -settings.getData().getEconomy().getMaxMoney() : settings.getData().getEconomy().getMaxMoney());
		}
		else
		{
			getData().setMoney(value);
		}
		queueSave();
	}

	public double getMoney()
	{
		Double money = getData().getMoney();

		final ISettings settings = ess.getSettings();

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

	public void setHome(String name, Location loc)
	{
		getData().addHome(Util.sanitizeKey(name), loc);
		queueSave();
	}

	public boolean toggleAfk()
	{
		boolean ret = !getData().isAfk();
		getData().setAfk(ret);
		queueSave();
		return ret;
	}

	public void setGodModeEnabled(boolean set)
	{
		getData().setGodmode(set);
		queueSave();
	}

	public void setMuted(boolean mute)
	{
		getData().setMuted(mute);
		queueSave();
	}

	public boolean toggleSocialSpy()
	{
		boolean ret = !getData().isSocialspy();
		getData().setSocialspy(ret);
		queueSave();
		return ret;
	}

	public boolean toggleTeleportEnabled()
	{
		boolean ret = !getData().isTeleportEnabled();
		getData().setTeleportEnabled(ret);
		queueSave();
		return ret;
	}

	public boolean isIgnoringPlayer(final IUser user)
	{
		return getData().getIgnore() == null ? false : getData().getIgnore().contains(user.getName().toLowerCase(Locale.ENGLISH)) && Permissions.CHAT_IGNORE_EXEMPT.isAuthorized(user);
	}

	public void setIgnoredPlayer(final IUser user, final boolean set)
	{
		getData().setIgnore(user.getName().toLowerCase(Locale.ENGLISH), set);
		queueSave();
	}

	public void addMail(String string)
	{
		getData().addMail(string);
		queueSave();
	}

	public List<String> getMails()
	{

		return getData().getMails();
	}

	public Location getHome(String name) throws Exception
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

	public Location getHome(Location loc)
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

	public Set<String> getHomes()
	{
		return getData().getHomes().keySet();
	}
}
