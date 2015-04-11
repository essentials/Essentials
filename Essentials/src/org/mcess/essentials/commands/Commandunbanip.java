package org.mcess.essentials.commands;

import org.mcess.essentials.CommandSource;
import org.mcess.essentials.Console;
import org.mcess.essentials.User;
import org.mcess.essentials.utils.FormatUtil;
import java.util.logging.Level;
import org.bukkit.BanList;
import org.bukkit.Server;
import org.mcess.essentials.I18n;


public class Commandunbanip extends EssentialsCommand
{
	public Commandunbanip()
	{
		super("unbanip");
	}

	@Override
	public void run(final Server server, final CommandSource sender, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 1)
		{
			throw new NotEnoughArgumentsException();
		}

		String ipAddress;
		if (FormatUtil.validIP(args[0]))
		{
			ipAddress = args[0];
		}
		else
		{
			try
			{
				User player = getPlayer(server, args, 0, true, true);
				ipAddress = player.getLastLoginAddress();
			}
			catch (PlayerNotFoundException ex)
			{
				ipAddress = args[0];
			}
		}

		if (ipAddress.isEmpty())
		{
			throw new PlayerNotFoundException();
		}


		ess.getServer().getBanList(BanList.Type.IP).pardon(ipAddress);
		final String senderName = sender.isPlayer() ? sender.getPlayer().getDisplayName() : Console.NAME;
		server.getLogger().log(Level.INFO, I18n.tl("playerUnbanIpAddress", senderName, ipAddress));

		ess.broadcastMessage("essentials.ban.notify", I18n.tl("playerUnbanIpAddress", senderName, ipAddress));
	}
}
