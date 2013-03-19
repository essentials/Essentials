package com.earth2me.essentials.commands;

import static com.earth2me.essentials.I18n._;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.earth2me.essentials.User;


public class Commandburn extends EssentialsCommand
{
	public Commandburn()
	{
		super("burn");
	}

	@Override
	protected void run(final Server server, final CommandSender sender, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 2)
		{
			throw new NotEnoughArgumentsException();
		}
		
		//TODO: TL this
		if (args[0].trim().length() < 2)
		{
			throw new NotEnoughArgumentsException("You need to specify a player to burn.");
		}

		User user = getPlayer(server, args, 0);
		user.setFireTicks(Integer.parseInt(args[1]) * 20);
		sender.sendMessage(_("burnMsg", user.getDisplayName(), Integer.parseInt(args[1])));
	}
}
