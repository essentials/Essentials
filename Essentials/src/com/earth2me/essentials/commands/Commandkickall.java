package com.earth2me.essentials.commands;

import static com.earth2me.essentials.I18n._;
import com.earth2me.essentials.api.server.ICommandSender;
import com.earth2me.essentials.api.server.Player;


public class Commandkickall extends EssentialsCommand
{
	@Override
	protected void run(final ICommandSender sender, final String commandLabel, final String[] args) throws Exception
	{
		for (Player onlinePlayer : server.getOnlinePlayers())
		{
			if (sender.isPlayer() && onlinePlayer.getName().equalsIgnoreCase(((Player)sender).getName()))
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
