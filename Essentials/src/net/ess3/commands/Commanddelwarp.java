package net.ess3.commands;

import static net.ess3.I18n._;
import org.bukkit.command.CommandSender;


public class Commanddelwarp extends EssentialsCommand
{
	@Override
	protected void run(final CommandSender sender, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 1)
		{
			throw new NotEnoughArgumentsException();
		}
		ess.getWarps().removeWarp(args[0]);
		sender.sendMessage(_("deleteWarp", args[0]));
	}
}
