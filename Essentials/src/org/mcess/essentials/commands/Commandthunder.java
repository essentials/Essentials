package org.mcess.essentials.commands;

import org.mcess.essentials.User;
import org.bukkit.Server;
import org.bukkit.World;
import org.mcess.essentials.I18n;


public class Commandthunder extends EssentialsCommand
{
	public Commandthunder()
	{
		super("thunder");
	}

	@Override
	public void run(final Server server, final User user, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 1)
		{
			throw new NotEnoughArgumentsException();
		}

		final World world = user.getWorld();
		final boolean setThunder = args[0].equalsIgnoreCase("true");
		if (args.length > 1)
		{

			world.setThundering(setThunder ? true : false);
			world.setThunderDuration(Integer.parseInt(args[1]) * 20);
			user.sendMessage(I18n.tl("thunderDuration", (setThunder ? I18n.tl("enabled") : I18n.tl("disabled")), Integer.parseInt(args[1])));

		}
		else
		{
			world.setThundering(setThunder ? true : false);
			user.sendMessage(I18n.tl("thunder", setThunder ? I18n.tl("enabled") : I18n.tl("disabled")));
		}

	}
}
