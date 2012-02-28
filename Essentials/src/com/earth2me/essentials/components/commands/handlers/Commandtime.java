package com.earth2me.essentials.components.commands.handlers;

import com.earth2me.essentials.DescParseTickFormat;
import com.earth2me.essentials.components.commands.EssentialsCommand;
import com.earth2me.essentials.components.commands.NotEnoughArgumentsException;
import static com.earth2me.essentials.components.i18n.I18nComponent._;
import com.earth2me.essentials.components.settings.users.IUserComponent;
import com.earth2me.essentials.perm.Permissions;
import java.util.*;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class Commandtime extends EssentialsCommand
{
	@Override
	public void run(final CommandSender sender, final String commandLabel, final String[] args) throws Exception
	{
		// Which World(s) are we interested in?
		String worldSelector = null;
		if (args.length == 2)
		{
			worldSelector = args[1];
		}
		final Set<World> worlds = getWorlds(sender, worldSelector);

		// If no arguments we are reading the time
		if (args.length == 0)
		{
			getWorldsTime(sender, worlds);
			return;
		}

		if (Permissions.TIME_SET.isAuthorized(sender))
		{
			sender.sendMessage(_("timeSetPermission"));
			return;
		}

		// Parse the target time int ticks from args[0]
		long ticks;
		try
		{
			ticks = DescParseTickFormat.parse(args[0]);
		}
		catch (NumberFormatException e)
		{
			throw new NotEnoughArgumentsException();
		}

		setWorldsTime(sender, worlds, ticks);
	}

	/**
	 * Used to get the time and inform
	 */
	private void getWorldsTime(final CommandSender sender, final Collection<World> worlds)
	{
		if (worlds.size() == 1)
		{
			final Iterator<World> iter = worlds.iterator();
			sender.sendMessage(DescParseTickFormat.format(iter.next().getTime()));
			return;
		}

		for (World world : worlds)
		{
			sender.sendMessage(_("timeWorldCurrent", world.getName(), DescParseTickFormat.format(world.getTime())));
		}
	}

	/**
	 * Used to set the time and inform of the change
	 */
	private void setWorldsTime(final CommandSender sender, final Collection<World> worlds, final long ticks)
	{
		// Update the time
		for (World world : worlds)
		{
			long time = world.getTime();
			time -= time % 24000;
			world.setTime(time + 24000 + ticks);
		}

		final StringBuilder output = new StringBuilder();
		for (World world : worlds)
		{
			if (output.length() > 0)
			{
				output.append(", ");
			}

			output.append(world.getName());
		}

		sender.sendMessage(_("timeWorldSet", DescParseTickFormat.format(ticks), output.toString()));
	}

	/**
	 * Used to parse an argument of the type "world(s) selector"
	 */
	private Set<World> getWorlds(final CommandSender sender, final String selector) throws Exception
	{
		final Set<World> worlds = new TreeSet<World>(new WorldNameComparator());

		// If there is no selector we want the world the user is currently in. Or all worlds if it isn't a user.
		if (selector == null)
		{
			final IUserComponent user = sender instanceof Player ? getContext().getUser((Player)sender) : null;
			if (user == null)
			{
				worlds.addAll(getServer().getWorlds());
			}
			else
			{
				worlds.add(user.getWorld());
			}
			return worlds;
		}

		// Try to find the world with name = selector
		final World world = getServer().getWorld(selector);
		if (world != null)
		{
			worlds.add(world);
		}
		// If that fails, Is the argument something like "*" or "all"?
		else if (selector.equalsIgnoreCase("*") || selector.equalsIgnoreCase("all"))
		{
			worlds.addAll(getServer().getWorlds());
		}
		// We failed to understand the world target...
		else
		{
			throw new Exception(_("invalidWorld"));
		}

		return worlds;
	}
}


class WorldNameComparator implements Comparator<World>
{
	@Override
	public int compare(final World a, final World b)
	{
		return a.getName().compareTo(b.getName());
	}
}
