package net.ess3.commands;

import static net.ess3.I18n._;
import net.ess3.api.IUser;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class Commandkickall extends EssentialsCommand
{
	@Override
	protected void run(final CommandSender sender, final String commandLabel, final String[] args) throws Exception
	{
		for (Player onlinePlayer : server.getOnlinePlayers())
		{
			if (sender instanceof IUser && onlinePlayer.getName().equalsIgnoreCase(((Player)sender).getName()))
			{
				continue;
			}
			else
			{
				onlinePlayer.kickPlayer(args.length > 0 ? getFinalArg(args, 0) : _("kickDefault"));
			}
		}
	}
}
