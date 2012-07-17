package net.ess3.commands;

import static net.ess3.I18n._;
import net.ess3.api.IUser;
import net.ess3.api.server.Location;
import net.ess3.utils.LocationUtil;
import org.bukkit.TreeType;


public class Commandbigtree extends EssentialsCommand
{
	@Override
	public void run(final IUser user, final String commandLabel, final String[] args) throws Exception
	{
		TreeType tree;
		if (args.length > 0 && args[0].equalsIgnoreCase("redwood"))
		{
			tree = TreeType.TALL_REDWOOD;
		}
		else if (args.length > 0 && args[0].equalsIgnoreCase("tree"))
		{
			tree = TreeType.BIG_TREE;
		}
		else if (args.length > 0 && args[0].equalsIgnoreCase("jungle"))
		{
			tree = TreeType.JUNGLE;
		}
		else
		{
			throw new NotEnoughArgumentsException();
		}

		final Location loc = LocationUtil.getTarget(user);
		final Location safeLocation = LocationUtil.getSafeDestination(loc);
		final boolean success = user.getWorld().generateTree(safeLocation, tree);
		if (success)
		{
			user.sendMessage(_("bigTreeSuccess"));
		}
		else
		{
			throw new Exception(_("bigTreeFailure"));
		}
	}
}
