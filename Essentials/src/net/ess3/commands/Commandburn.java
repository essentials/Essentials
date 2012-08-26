package net.ess3.commands;

import static net.ess3.I18n._;
import net.ess3.api.IUser;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class Commandburn extends EssentialsCommand
{
	@Override
	protected void run(final CommandSender sender, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 2)
		{
			throw new NotEnoughArgumentsException();
		}

		for (IUser p : ess.getUserMap().matchUsers(args[0], false, false))
		{
			p.getPlayer().setFireTicks(Integer.parseInt(args[1]) * 20);
			sender.sendMessage(_("burnMsg", p.getPlayer().getDisplayName(), Integer.parseInt(args[1])));
		}
	}
}
