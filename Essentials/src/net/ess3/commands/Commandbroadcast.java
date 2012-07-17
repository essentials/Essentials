package net.ess3.commands;

import static net.ess3.I18n._;
import net.ess3.api.server.CommandSender;
import net.ess3.utils.Util;



public class Commandbroadcast extends EssentialsCommand
{
	@Override
	public void run(final CommandSender sender, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 1)
		{
			throw new NotEnoughArgumentsException();
		}
		ess.broadcastMessage(null, _("broadcast", Util.replaceFormat(getFinalArg(args, 0))));
	}
}
