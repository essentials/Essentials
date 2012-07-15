package com.earth2me.essentials.commands;

import static com.earth2me.essentials.I18n._;
import com.earth2me.essentials.api.server.CommandSender;
import org.bukkit.command.CommandSender;


public class Commandbroadcast extends EssentialsCommand
{
	@Override
	protected void run(final CommandSender sender, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 1)
		{
			throw new NotEnoughArgumentsException();
		}

		ess.broadcastMessage(null, _("broadcast", getFinalArg(args, 0)));
	}
}
