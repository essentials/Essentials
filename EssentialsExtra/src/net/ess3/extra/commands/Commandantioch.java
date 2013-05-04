package net.ess3.extra.commands;

import net.ess3.api.IUser;
import net.ess3.commands.EssentialsCommand;
import net.ess3.utils.LocationUtil;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.entity.TNTPrimed;

// This command has a theme message that only shows if you supply a parameter #EasterEgg
public class Commandantioch extends EssentialsCommand
{
	public void run(final Server server, final IUser user, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length > 0)
		{
			ess.broadcastMessage(user, "...lobbest thou thy Holy Hand Grenade of Antioch towards thy foe,");
			ess.broadcastMessage(user, "who being naughty in My sight, shall snuff it.");
		}		

		final Location loc = LocationUtil.getTarget(user.getPlayer());
		loc.getWorld().spawn(loc, TNTPrimed.class);
	}
}