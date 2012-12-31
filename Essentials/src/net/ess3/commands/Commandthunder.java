package net.ess3.commands;

import static net.ess3.I18n._;
import org.bukkit.World;
import net.ess3.api.IUser;


public class Commandthunder extends EssentialsCommand
{
	@Override
	public void run(final IUser user, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 1)
		{
			throw new NotEnoughArgumentsException();
		}

		final World world = user.getPlayer().getWorld();
		final boolean setThunder = args[0].equalsIgnoreCase("true");
		if (args.length > 1)
		{

			world.setThundering(setThunder ? true : false);
			world.setThunderDuration(Integer.parseInt(args[1]) * 20);
			user.sendMessage(_("thunderDuration", (setThunder ? _("enabled") : _("disabled")), Integer.parseInt(args[1])));

		}
		else
		{
			world.setThundering(setThunder ? true : false);
			user.sendMessage(_("thunder", setThunder ? _("enabled") : _("disabled")));
		}

	}
}
