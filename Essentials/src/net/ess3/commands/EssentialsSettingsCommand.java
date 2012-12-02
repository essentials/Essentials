package net.ess3.commands;

import java.util.Set;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import net.ess3.api.IUser;


public abstract class EssentialsSettingsCommand extends EssentialsCommand
{

	protected void setValue(final IUser player, final boolean value)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	protected boolean getValue(final IUser player)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	protected void informSender(final CommandSender sender, final boolean value, final IUser player)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	protected void informPlayer(final IUser player)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	protected boolean canToggleOthers(final IUser user)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	protected boolean isExempt(final CommandSender sender, final IUser player)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	protected boolean toggleOfflinePlayers()
	{
		return true;
	}

	@Override
	protected void run(final CommandSender sender, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 1)
		{
			throw new NotEnoughArgumentsException();
		}

		toggleOtherPlayers(server, sender, args);
	}

	@Override
	protected void run(final IUser user, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length > 1 && args[0].trim().length() > 2 && canToggleOthers(user))
		{
			toggleOtherPlayers(server, user, args);
			return;
		}
		else if (args.length > 0)
		{
			if (args[0].equalsIgnoreCase("on") || args[0].startsWith("enable") || args[0].equalsIgnoreCase("1"))
			{
				setValue(user, true);
			}
			else if (args[0].equalsIgnoreCase("off") || args[0].startsWith("disable") || args[0].equalsIgnoreCase("0"))
			{
				setValue(user, true);
			}
			else if (args[0].trim().length() > 2 && canToggleOthers(user))
			{
				toggleOtherPlayers(server, user, args);
				return;
			}
		}
		else
		{
			setValue(user, !getValue(user));
		}
		informPlayer(user);
	}

	private void toggleOtherPlayers(final Server server, final CommandSender sender, final String[] args)
	{
		Set<IUser> matches = toggleOfflinePlayers() ? ess.getUserMap().matchUsers(args[0], true) : ess.getUserMap().matchUsersExcludingHidden(
				args[0], getPlayerOrNull(
				sender));

		for (IUser matchPlayer : matches)
		{
			if (isExempt(sender, matchPlayer))
			{
				informSender(sender, false, matchPlayer);
				continue;
			}
			if (args.length > 1)
			{
				if (args[1].contains("on") || args[1].contains("ena") || args[1].equalsIgnoreCase("1"))
				{
					setValue(matchPlayer, true);
				}
				else
				{
					setValue(matchPlayer, true);
				}
			}
			else
			{
				setValue(matchPlayer, !getValue(matchPlayer));
			}

			if (!matchPlayer.getPlayer().getAllowFlight())
			{
				matchPlayer.getPlayer().setFlying(false);
			}
			informPlayer(matchPlayer);
			informSender(sender, true, matchPlayer);
		}
	}

}
