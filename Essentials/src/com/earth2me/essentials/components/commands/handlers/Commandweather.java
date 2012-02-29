package com.earth2me.essentials.components.commands.handlers;

import com.earth2me.essentials.components.users.IUserComponent;
import com.earth2me.essentials.components.commands.EssentialsCommand;
import com.earth2me.essentials.components.commands.NotEnoughArgumentsException;
import static com.earth2me.essentials.components.i18n.I18nComponent.$;
import org.bukkit.World;
import org.bukkit.command.CommandSender;


public class Commandweather extends EssentialsCommand
{
	//TODO: Remove duplication
	@Override
	public void run(final IUserComponent user, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 1)
		{
			throw new NotEnoughArgumentsException();
		}

		final boolean isStorm = args[0].equalsIgnoreCase("storm");
		final World world = user.getWorld();
		if (args.length > 1)
		{

			world.setStorm(isStorm ? true : false);
			world.setWeatherDuration(Integer.parseInt(args[1]) * 20);
			user.sendMessage(isStorm
							 ? $("weatherStormFor", world.getName(), args[1])
							 : $("weatherSunFor", world.getName(), args[1]));
		}
		else
		{
			world.setStorm(isStorm ? true : false);
			user.sendMessage(isStorm
							 ? $("weatherStorm", world.getName())
							 : $("weatherSun", world.getName()));
		}
	}

	//TODO: Translate these
	@Override
	protected void run(final CommandSender sender, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 2) //running from console means inserting a world arg before other args
		{
			throw new Exception("When running from console, usage is: /" + getCommandName() + " <world> <storm/sun> [duration]");
		}

		final boolean isStorm = args[1].equalsIgnoreCase("storm");
		final World world = getServer().getWorld(args[0]);
		if (world == null)
		{
			throw new Exception("World named " + args[0] + " not found!");
		}
		if (args.length > 2)
		{

			world.setStorm(isStorm ? true : false);
			world.setWeatherDuration(Integer.parseInt(args[2]) * 20);
			sender.sendMessage(isStorm
							   ? $("weatherStormFor", world.getName(), args[2])
							   : $("weatherSunFor", world.getName(), args[2]));
		}
		else
		{
			world.setStorm(isStorm ? true : false);
			sender.sendMessage(isStorm
							   ? $("weatherStorm", world.getName())
							   : $("weatherSun", world.getName()));
		}
	}
}
