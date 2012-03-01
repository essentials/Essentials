package com.earth2me.essentials.components.commands.handlers;

import com.earth2me.essentials.components.commands.EssentialsCommand;
import com.earth2me.essentials.components.commands.NotEnoughArgumentsException;
import static com.earth2me.essentials.components.i18n.I18nComponent.$;
import com.earth2me.essentials.components.users.IUserComponent;
import org.bukkit.World;


public class Commandthunder extends EssentialsCommand
{
	@Override
	public void run(final IUserComponent user, final String commandLabel, final String[] args) throws Exception
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
			user.sendMessage($("thunderDuration", (setThunder ? $("enabled") : $("disabled")), Integer.parseInt(args[1])));

		}
		else
		{
			world.setThundering(setThunder ? true : false);
			user.sendMessage($("thunder", setThunder ? $("enabled") : $("disabled")));
		}

	}
}
