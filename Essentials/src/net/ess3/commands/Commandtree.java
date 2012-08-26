package net.ess3.commands;

import static net.ess3.I18n._;
import net.ess3.api.IUser;
import net.ess3.utils.LocationUtil;
import org.bukkit.Location;
import org.bukkit.TreeType;


public class Commandtree extends EssentialsCommand
{
	@Override
	public void run(final IUser user, final String commandLabel, final String[] args) throws Exception
	{
		TreeType tree;
		if (args.length < 1)
		{
			throw new NotEnoughArgumentsException();
		}
		else if (args[0].equalsIgnoreCase("birch"))
		{
			tree = TreeType.BIRCH;
		}
		else if (args[0].equalsIgnoreCase("redwood"))
		{
			tree = TreeType.REDWOOD;
		}
		else if (args[0].equalsIgnoreCase("tree"))
		{
			tree = TreeType.TREE;
		}
		else if (args[0].equalsIgnoreCase("redmushroom"))
		{
			tree = TreeType.RED_MUSHROOM;
		}
		else if (args[0].equalsIgnoreCase("brownmushroom"))
		{
			tree = TreeType.BROWN_MUSHROOM;
		}
		else if (args[0].equalsIgnoreCase("jungle"))
		{
			tree = TreeType.SMALL_JUNGLE;
		}
		else if (args[0].equalsIgnoreCase("junglebush"))
		{
			tree = TreeType.JUNGLE_BUSH;
					}
		else if (args[0].equalsIgnoreCase("swamp"))
		{
			tree = TreeType.SWAMP;
		}
		else
		{
			throw new NotEnoughArgumentsException();
		}

		final Location loc = LocationUtil.getTarget(user.getPlayer());
		final Location safeLocation = LocationUtil.getSafeDestination(loc);
		final boolean success = user.getPlayer().getWorld().generateTree(safeLocation, tree);
		if (success)
		{
			user.sendMessage(_("treeSpawned"));
		}
		else
		{
			user.sendMessage(_("treeFailure"));
		}
	}
}
