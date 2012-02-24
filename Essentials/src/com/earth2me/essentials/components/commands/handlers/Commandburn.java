package com.earth2me.essentials.components.commands.handlers;

import com.earth2me.essentials.components.commands.EssentialsCommand;
import com.earth2me.essentials.components.commands.NotEnoughArgumentsException;
import static com.earth2me.essentials.components.i18n.I18nComponent._;
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

		for (Player p : getServer().matchPlayer(args[0]))
		{
			p.setFireTicks(Integer.parseInt(args[1]) * 20);
			sender.sendMessage(_("burnMsg", p.getDisplayName(), Integer.parseInt(args[1])));
		}
	}
}
