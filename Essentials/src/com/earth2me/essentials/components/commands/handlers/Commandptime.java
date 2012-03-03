package com.earth2me.essentials.components.commands.handlers;

import com.earth2me.essentials.DescParseTickFormat;
import com.earth2me.essentials.components.commands.EssentialsCommand;
import com.earth2me.essentials.components.commands.NotEnoughArgumentsException;
import static com.earth2me.essentials.components.i18n.I18nComponent._;
import com.earth2me.essentials.components.users.IUserComponent;
import com.earth2me.essentials.perm.Permissions;
import java.util.*;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class Commandptime extends EssentialsCommand
{
	public static final Set<String> getAliases = new HashSet<String>();

	static
	{
		getAliases.add("get");
		getAliases.add("list");
		getAliases.add("show");
		getAliases.add("display");
	}

	@Override
	public void run(final CommandSender sender, final String commandLabel, final String[] args) throws Exception
	{
		// Which Players(s) / Users(s) are we interested in?
		String userSelector = null;
		if (args.length == 2)
		{
			userSelector = args[1];
		}
		Set<IUserComponent> users = getUsers(sender, userSelector);

		// If no arguments we are reading the time
		if (args.length == 0)
		{
			getUsersTime(sender, users);
			return;
		}

		IUserComponent user = sender instanceof Player ? getContext().getUser((Player)sender) : null;
		if ((!users.contains(user) || users.size() > 1) && user != null && !Permissions.PTIME_OTHERS.isAuthorized(user))
		{
			user.sendMessage(_("pTimeOthersPermission"));
			return;
		}

		Long ticks;
		// Parse the target time int ticks from args[0]
		String timeParam = args[0];
		Boolean relative = true;
		if (timeParam.startsWith("@"))
		{
			relative = false;
			timeParam = timeParam.substring(1);
		}

		if (getAliases.contains(timeParam))
		{
			getUsersTime(sender, users);
			return;
		}
		else if (DescParseTickFormat.meansReset(timeParam))
		{
			ticks = null;
		}
		else
		{
			try
			{
				ticks = DescParseTickFormat.parse(timeParam);
			}
			catch (NumberFormatException e)
			{
				throw new NotEnoughArgumentsException();
			}
		}

		setUsersTime(sender, users, ticks, relative);
	}

	/**
	 * Used to get the time and inform
	 */
	private void getUsersTime(final CommandSender sender, final Collection<IUserComponent> users)
	{
		if (users.size() > 1)
		{
			sender.sendMessage(_("pTimePlayers"));
		}

		for (IUserComponent user : users)
		{
			if (user.getPlayerTimeOffset() == 0)
			{
				sender.sendMessage(_("pTimeNormal", user.getName()));
			}
			else
			{
				String time = DescParseTickFormat.format(user.getPlayerTime());
				if (!user.isPlayerTimeRelative())
				{
					sender.sendMessage(_("pTimeCurrentFixed", user.getName(), time));
				}
				else
				{
					sender.sendMessage(_("pTimeCurrent", user.getName(), time));
				}
			}
		}
	}

	/**
	 * Used to set the time and inform of the change
	 */
	private void setUsersTime(final CommandSender sender, final Collection<IUserComponent> users, final Long ticks, Boolean relative)
	{
		// Update the time
		if (ticks == null)
		{
			// Reset
			for (IUserComponent user : users)
			{
				user.resetPlayerTime();
			}
		}
		else
		{
			// Set
			for (IUserComponent user : users)
			{
				final World world = user.getWorld();
				long time = user.getPlayerTime();
				time -= time % 24000;
				time += 24000 + ticks;
				if (relative)
				{
					time -= world.getTime();
				}
				user.setPlayerTime(time, relative);
			}
		}

		final StringBuilder msg = new StringBuilder();
		for (IUserComponent user : users)
		{
			if (msg.length() > 0)
			{
				msg.append(", ");
			}

			msg.append(user.getName());
		}

		// Inform the sender of the change
		if (ticks == null)
		{
			sender.sendMessage(_("pTimeReset", msg.toString()));
		}
		else
		{
			String time = DescParseTickFormat.format(ticks);
			if (!relative)
			{
				sender.sendMessage(_("pTimeSetFixed", time, msg.toString()));
			}
			else
			{
				sender.sendMessage(_("pTimeSet", time, msg.toString()));
			}
		}
	}

	/**
	 * Used to parse an argument of the type "users(s) selector"
	 */
	private Set<IUserComponent> getUsers(final CommandSender sender, final String selector) throws Exception
	{
		final Set<IUserComponent> users = new TreeSet<IUserComponent>();
		// If there is no selector we want the sender itself. Or all users if sender isn't a user.
		if (selector == null)
		{
			final IUserComponent user = sender instanceof Player ? getContext().getUser((Player)sender) : null;
			if (user == null)
			{
				for (Player player : getServer().getOnlinePlayers())
				{
					users.add(getContext().getUser(player));
				}
			}
			else
			{
				users.add(user);
			}
			return users;
		}

		// Try to find the user with name = selector
		IUserComponent user = null;
		final List<Player> matchedPlayers = getServer().matchPlayer(selector);
		if (!matchedPlayers.isEmpty())
		{
			user = getContext().getUser(matchedPlayers.get(0));
		}

		if (user != null)
		{
			users.add(user);
		}
		// If that fails, Is the argument something like "*" or "all"?
		else if (selector.equalsIgnoreCase("*") || selector.equalsIgnoreCase("all"))
		{
			for (Player player : getServer().getOnlinePlayers())
			{
				users.add(getContext().getUser(player));
			}
		}
		// We failed to understand the world target...
		else
		{
			throw new Exception(_("playerNotFound"));
		}

		return users;
	}
}
