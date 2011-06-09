package com.earth2me.essentials.permissions;

import com.earth2me.essentials.commands.EssentialsCommand;
import com.earth2me.essentials.commands.NotEnoughArgumentsException;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;


public class Commandmanuaddsub extends EssentialsCommand
{
	public Commandmanuaddsub()
	{
		super("manuaddsub");
	}

	@Override
	protected void run(Server server, CommandSender sender, String commandLabel, String[] args) throws Exception
	{
		if (args.length < 2)
		{
			throw new NotEnoughArgumentsException();
		}
		final String player = args[0];
		final String group = args[1];
		ess.getServer().dispatchCommand(sender, "/permissions "+player+" parents add "+group+"");
	}
	
	
}
