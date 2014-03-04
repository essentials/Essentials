package com.earth2me.essentials.commands;

import com.earth2me.essentials.CommandSource;
import org.bukkit.Server;

import static com.earth2me.essentials.I18n._;


public class Commanddelwarp extends EssentialsCommand
{
	public Commanddelwarp()
	{
		super("delwarp");
	}

	@Override
	public void run(final Server server, final CommandSource sender, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 1)
		{
			throw new NotEnoughArgumentsException();
		}

		if (ess.getWarps().getList().contains(args[0]))
		{
			ess.getWarps().removeWarp(args[0]);
			sender.sendMessage(_("deleteWarp", args[0]));
		}
		else
		{
			sender.sendMessage(_("invalidWarpName"));
		}
	}
}
