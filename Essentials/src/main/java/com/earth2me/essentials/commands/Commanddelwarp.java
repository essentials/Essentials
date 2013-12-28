package com.earth2me.essentials.commands;

import com.earth2me.essentials.CommandSource;
import static com.earth2me.essentials.I18n._;
import org.bukkit.Server;


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
		
		ess.getWarps().removeWarp(args[0]);
		sender.sendMessage(_("deleteWarp", args[0]));
	}
}
