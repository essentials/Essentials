package net.ess3.ranks;

import java.util.logging.Level;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import net.ess3.api.IEssentials;
import net.ess3.api.IRanks;
import net.ess3.api.ISettings;
import org.anjocaido.groupmanager.GroupManager;
import org.anjocaido.groupmanager.permissions.AnjoPermissionsHandler;


public class GMGroups extends AbstractRanks implements IRanks
{
	private final transient IEssentials ess;
	private final transient GroupManager groupManager;

	public GMGroups(final IEssentials ess, final Plugin groupManager)
	{
		this.ess = ess;
		this.groupManager = (GroupManager)groupManager;
	}

	private double getGMDouble(final CommandSender player, final String key, final double defaultValue)
	{
		try
		{
			final AnjoPermissionsHandler handler = groupManager.getWorldsHolder().getWorldPermissions(getPlayer(player));
			if (handler == null)
			{
				return defaultValue;
			}
			return handler.getPermissionDouble(player.getName(), key);
		}
		catch (Exception e)
		{
			ess.getLogger().log(Level.WARNING, e.getMessage(), e);
			return defaultValue;
		}
	}

	private int getGMInteger(final CommandSender player, final String key, final int defaultValue)
	{
		try
		{
			final AnjoPermissionsHandler handler = groupManager.getWorldsHolder().getWorldPermissions(getPlayer(player));
			if (handler == null)
			{
				return defaultValue;
			}
			return handler.getPermissionInteger(player.getName(), key);
		}
		catch (Exception e)
		{
			ess.getLogger().log(Level.WARNING, e.getMessage(), e);
			return defaultValue;
		}
	}

	private String getGMString(final CommandSender player, final String key, final String defaultValue)
	{
		try
		{
			final AnjoPermissionsHandler handler = groupManager.getWorldsHolder().getWorldPermissions(getPlayer(player));
			if (handler == null)
			{
				return defaultValue;
			}
			return handler.getPermissionString(player.getName(), key);
		}
		catch (Exception e)
		{
			ess.getLogger().log(Level.WARNING, e.getMessage(), e);
			return defaultValue;
		}
	}

	@Override
	public double getHealCooldown(final CommandSender player)
	{
		return getGMDouble(player, "healcooldown", 0);
	}

	@Override
	public double getTeleportCooldown(final CommandSender player)
	{
		return getGMDouble(player, "teleportcooldown", 0);
	}

	@Override
	public double getTeleportDelay(final CommandSender player)
	{
		return getGMDouble(player, "teleportdelay", 0);
	}

	@Override
	public String getPrefix(final CommandSender player)
	{
		try
		{
			final AnjoPermissionsHandler handler = groupManager.getWorldsHolder().getWorldPermissions(getPlayer(player));
			if (handler == null)
			{
				return null;
			}
			return handler.getUserPrefix(player.getName());
		}
		catch (Exception e)
		{
			ess.getLogger().log(Level.WARNING, e.getMessage(), e);
			return null;
		}
	}

	@Override
	public String getSuffix(final CommandSender player)
	{
		try
		{
			final AnjoPermissionsHandler handler = groupManager.getWorldsHolder().getWorldPermissions(getPlayer(player));
			if (handler == null)
			{
				return null;
			}
			return handler.getUserSuffix(player.getName());
		}
		catch (Exception e)
		{
			ess.getLogger().log(Level.WARNING, e.getMessage(), e);
			return null;
		}
	}

	@Override
	public int getHomeLimit(final CommandSender player)
	{
		return getGMInteger(player, "homes", 1);
	}

	@Override
	protected String getRawChatFormat(final CommandSender player)
	{
		String chatformat = getGMString(player, "chatformat", null);
		if (chatformat != null && !chatformat.isEmpty())
		{
			return chatformat;
		}

		ISettings settings = ess.getSettings();
		return settings.getData().getChat().getDefaultFormat();
	}

	@Override
	public String getMainGroup(CommandSender player)
	{
		try
		{
			final AnjoPermissionsHandler handler = groupManager.getWorldsHolder().getWorldPermissions(getPlayer(player));
			if (handler == null)
			{
				return null;
			}
			return handler.getGroup(player.getName());
		}
		catch (Exception e)
		{
			ess.getLogger().log(Level.WARNING, e.getMessage(), e);
			return null;
		}
	}

	@Override
	public boolean inGroup(CommandSender player, String groupname)
	{
		try
		{
			final AnjoPermissionsHandler handler = groupManager.getWorldsHolder().getWorldPermissions(getPlayer(player));
			if (handler == null)
			{
				return false;
			}
			return handler.inGroup(player.getName(), groupname);
		}
		catch (Exception e)
		{
			ess.getLogger().log(Level.WARNING, e.getMessage(), e);
			return false;
		}
	}
}
