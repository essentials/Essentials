package net.ess3.ranks;

import net.ess3.api.IEssentials;
import net.ess3.api.IRanks;
import net.ess3.api.ISettings;
import org.anjocaido.groupmanager.GroupManager;
import org.anjocaido.groupmanager.permissions.AnjoPermissionsHandler;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;


public class GMGroups extends AbstractRanks implements IRanks
{
	private final transient IEssentials ess;
	private final transient GroupManager groupManager;

	public GMGroups(final IEssentials ess, final Plugin groupManager)
	{
		this.ess = ess;
		this.groupManager = (GroupManager)groupManager;
	}
	
	@Override
	public double getHealCooldown(CommandSender player)
	{
		AnjoPermissionsHandler handler = groupManager.getWorldsHolder().getWorldPermissions(getPlayer(player));
		if (handler == null)
		{
			return 0;
		}
		return handler.getPermissionDouble(player.getName(), "healcooldown");
	}

	@Override
	public double getTeleportCooldown(CommandSender player)
	{
		AnjoPermissionsHandler handler = groupManager.getWorldsHolder().getWorldPermissions(getPlayer(player));
		if (handler == null)
		{
			return 0;
		}
		return handler.getPermissionDouble(player.getName(), "teleportcooldown");
	}

	@Override
	public double getTeleportDelay(CommandSender player)
	{
		AnjoPermissionsHandler handler = groupManager.getWorldsHolder().getWorldPermissions(getPlayer(player));
		if (handler == null)
		{
			return 0;
		}
		return handler.getPermissionDouble(player.getName(), "teleportdelay");
	}

	@Override
	public String getPrefix(CommandSender player)
	{
		AnjoPermissionsHandler handler = groupManager.getWorldsHolder().getWorldPermissions(getPlayer(player));
		if (handler == null)
		{
			return null;
		}
		return handler.getUserPrefix(player.getName());
	}

	@Override
	public String getSuffix(CommandSender player)
	{
		AnjoPermissionsHandler handler = groupManager.getWorldsHolder().getWorldPermissions(getPlayer(player));
		if (handler == null)
		{
			return null;
		}
		return handler.getUserSuffix(player.getName());
	}

	@Override
	public int getHomeLimit(CommandSender player)
	{
		AnjoPermissionsHandler handler = groupManager.getWorldsHolder().getWorldPermissions(getPlayer(player));
		if (handler == null)
		{
			return 0;
		}
		return handler.getPermissionInteger(player.getName(), "homes");
	}

	@Override
	protected String getRawChatFormat(final CommandSender player)
	{
		AnjoPermissionsHandler handler = groupManager.getWorldsHolder().getWorldPermissions(getPlayer(player));
		if (handler != null)
		{
			String chatformat = handler.getPermissionString(player.getName(), "chatformat");
			if (chatformat != null && !chatformat.isEmpty())
			{
				return chatformat;
			}
		}

		ISettings settings = ess.getSettings();
		return settings.getData().getChat().getDefaultFormat();
	}

	@Override
	public String getMainGroup(CommandSender player)
	{
		final AnjoPermissionsHandler handler = groupManager.getWorldsHolder().getWorldPermissions(getPlayer(player));
		if (handler == null)
		{
			return null;
		}
		return handler.getGroup(player.getName());
	}

	@Override
	public boolean inGroup(CommandSender player, String groupname)
	{
		final AnjoPermissionsHandler handler = groupManager.getWorldsHolder().getWorldPermissions(getPlayer(player));
		if (handler == null)
		{
			return false;
		}
		return handler.inGroup(player.getName(), groupname);
	}
}
