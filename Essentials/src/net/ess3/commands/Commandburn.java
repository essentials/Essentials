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

		for (IUser p : ess.getUserMap().matchUsersExcludingHidden(args[0], getPlayerOrNull(sender)))
		{
			final Player player = p.getPlayer();
			player.setFireTicks(Integer.parseInt(args[1]) * 20);
			sender.sendMessage(_("burnMsg", player.getDisplayName(), Integer.parseInt(args[1])));
		}
	}
}
