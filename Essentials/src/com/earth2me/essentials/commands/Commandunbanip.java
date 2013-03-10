package com.earth2me.essentials.commands;

import com.earth2me.essentials.Console;
import static com.earth2me.essentials.I18n._;
import com.earth2me.essentials.User;
import com.earth2me.essentials.Util;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class Commandunbanip extends EssentialsCommand
{
	public Commandunbanip()
	{
		super("unbanip");
	}

	@Override
	public void run(final Server server, final CommandSender sender, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 1)
		{
			throw new NotEnoughArgumentsException();
		}
		String ipAddress;
		if (Util.validIP(args[0]))
		{
			ipAddress = args[0];
		}
		else
		{
			final User user = getPlayer(server, args, 0, true);
			ipAddress = user.getLastLoginAddress();
			if (ipAddress.isEmpty())
			{
				throw new PlayerNotFoundException();
			}
		}
		
		ess.getServer().unbanIP(ipAddress);
		final String senderName = sender instanceof Player ? ((Player)sender).getDisplayName() : Console.NAME;
		for (Player onlinePlayer : server.getOnlinePlayers())
		{
			final User onlineUser = ess.getUser(onlinePlayer);
			if (onlinePlayer == sender || onlineUser.isAuthorized("essentials.ban.notify"))
			{
				onlinePlayer.sendMessage(_("playerUnbanIpAddress", senderName, ipAddress));
			}
		}
	}
}
