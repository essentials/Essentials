package org.mcess.essentials.commands;

import org.mcess.essentials.CommandSource;
import org.mcess.essentials.User;
import org.bukkit.Server;
import org.mcess.essentials.I18n;


public class Commandburn extends EssentialsCommand
{
	public Commandburn()
	{
		super("burn");
	}

	@Override
	protected void run(final Server server, final CommandSource sender, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 2)
		{
			throw new NotEnoughArgumentsException();
		}

		if (args[0].trim().length() < 2)
		{
			throw new NotEnoughArgumentsException();
		}

		User user = getPlayer(server, sender, args, 0);
		user.getBase().setFireTicks(Integer.parseInt(args[1]) * 20);
		sender.sendMessage(I18n.tl("burnMsg", user.getDisplayName(), Integer.parseInt(args[1])));
	}
}
