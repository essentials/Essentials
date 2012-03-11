package com.earth2me.essentials.anticheat.command;

import com.earth2me.essentials.anticheat.NoCheat;
import com.earth2me.essentials.anticheat.config.Permissions;
import java.util.*;
import java.util.Map.Entry;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;


/**
 * Handle all NoCheat related commands in a common place
 */
public class CommandHandler
{
	private final List<Permission> perms;

	public CommandHandler(NoCheat plugin)
	{
		// Make a copy to allow sorting
		perms = new LinkedList<Permission>(plugin.getDescription().getPermissions());

		// Sort NoCheats permission by name and parent-child relation with
		// a custom sorting method
		Collections.sort(perms, new Comparator<Permission>()
		{
			public int compare(Permission o1, Permission o2)
			{

				String name1 = o1.getName();
				String name2 = o2.getName();

				if (name1.equals(name2))
				{
					return 0;
				}

				if (name1.startsWith(name2))
				{
					return 1;
				}

				if (name2.startsWith(name1))
				{
					return -1;
				}

				return name1.compareTo(name2);
			}
		});
	}

	/**
	 * Handle a command that is directed at NoCheat
	 *
	 * @param plugin
	 * @param sender
	 * @param command
	 * @param label
	 * @param args
	 * @return
	 */
	public boolean handleCommand(NoCheat plugin, CommandSender sender, Command command, String label, String[] args)
	{

		boolean result = false;
		// Not our command, how did it get here?
		if (!command.getName().equalsIgnoreCase("nocheat") || args.length == 0)
		{
			result = false;
		}
		else if (args[0].equalsIgnoreCase("permlist") && args.length >= 2)
		{
			// permlist command was used
			result = handlePermlistCommand(plugin, sender, args);

		}
		else if (args[0].equalsIgnoreCase("reload"))
		{
			// reload command was used
			result = handleReloadCommand(plugin, sender);
		}
		else if (args[0].equalsIgnoreCase("playerinfo") && args.length >= 2)
		{
			// playerinfo command was used
			result = handlePlayerInfoCommand(plugin, sender, args);
		}

		return result;
	}

	private boolean handlePlayerInfoCommand(NoCheat plugin, CommandSender sender, String[] args)
	{

		Map<String, Object> map = plugin.getPlayerData(args[1]);
		String filter = "";

		if (args.length > 2)
		{
			filter = args[2];
		}

		sender.sendMessage("PlayerInfo for " + args[1]);
		for (Entry<String, Object> entry : map.entrySet())
		{
			if (entry.getKey().contains(filter))
			{
				sender.sendMessage(entry.getKey() + ": " + entry.getValue());
			}
		}
		return true;
	}

	private boolean handlePermlistCommand(NoCheat plugin, CommandSender sender, String[] args)
	{

		// Get the player by name
		Player player = plugin.getServer().getPlayerExact(args[1]);
		if (player == null)
		{
			sender.sendMessage("Unknown player: " + args[1]);
			return true;
		}

		// Should permissions be filtered by prefix?
		String prefix = "";
		if (args.length == 3)
		{
			prefix = args[2];
		}

		sender.sendMessage("Player " + player.getName() + " has the permission(s):");

		for (Permission permission : perms)
		{
			if (permission.getName().startsWith(prefix))
			{
				sender.sendMessage(permission.getName() + ": " + player.hasPermission(permission));
			}
		}
		return true;
	}

	private boolean handleReloadCommand(NoCheat plugin, CommandSender sender)
	{

		// Players need a special permission for this
		if (!(sender instanceof Player) || sender.hasPermission(Permissions.ADMIN_RELOAD))
		{
			sender.sendMessage("[NoCheat] Reloading configuration");
			plugin.reloadConfiguration();
			sender.sendMessage("[NoCheat] Configuration reloaded");
		}
		else
		{
			sender.sendMessage("You lack the " + Permissions.ADMIN_RELOAD + " permission to use 'reload'");
		}

		return true;
	}
}
