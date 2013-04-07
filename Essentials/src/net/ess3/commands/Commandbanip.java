package net.ess3.commands;

import static net.ess3.I18n._;
import net.ess3.api.IUser;
import org.bukkit.command.CommandSender;


public class Commandbanip extends EssentialsCommand
{
	@Override
	protected void run(final CommandSender sender, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 1)
		{
			throw new NotEnoughArgumentsException();
		}

		final IUser player = ess.getUserMap().getUser(args[0]);

		if (player == null)
		{
			if (args[0].matches("\\b(?:\\d{1,3}\\.){3}\\d{1,3}\\b"))
			{
				ess.getServer().banIP(args[0]);
				sender.sendMessage(_("Banned IP address"));
			}
			else
			{
				sender.sendMessage(_("invalidIpAddress"));
			}
		}
		else
		{
			final String ipAddress = player.getData().getIpAddress();
			if (ipAddress.length() == 0)
			{
				throw new Exception(_("§4Player not found."));
			}
			ess.getServer().banIP(ipAddress);
			sender.sendMessage(_("Banned IP address"));
		}
	}
}
