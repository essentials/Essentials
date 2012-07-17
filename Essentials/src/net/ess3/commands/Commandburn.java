package net.ess3.commands;

import static net.ess3.I18n._;
import net.ess3.api.server.CommandSender;
import net.ess3.api.server.Player;


public class Commandburn extends EssentialsCommand
{
	@Override
	protected void run(final CommandSender sender, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 2)
		{
			throw new NotEnoughArgumentsException();
		}

		for (Player p : server.matchPlayer(args[0]))
		{
			p.setFireTicks(Integer.parseInt(args[1]) * 20);
			sender.sendMessage(_("burnMsg", p.getDisplayName(), Integer.parseInt(args[1])));
		}
	}
}
