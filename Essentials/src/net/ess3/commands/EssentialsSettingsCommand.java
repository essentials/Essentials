package net.ess3.commands;

import java.util.Set;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import net.ess3.api.IUser;


public abstract class EssentialsSettingsCommand extends EssentialsCommand
{

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

	protected boolean canMatch(final String arg)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	protected void playerMatch(final IUser player, final String arg) throws NotEnoughArgumentsException
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
			if (canMatch(args[0]))
			{
				playerMatch(user, args[0]);
			}
			else if (args[0].trim().length() > 2 && canToggleOthers(user))
			{
				toggleOtherPlayers(server, user, args);
				return;
			}
		}
		else
		{
			playerMatch(user, null);
		}
		informPlayer(user);
	}

	private void toggleOtherPlayers(final Server server, final CommandSender sender, final String[] args) throws NotEnoughArgumentsException
	{
		Set<IUser> matches = toggleOfflinePlayers() ? ess.getUserMap().matchUsers(args[0], true) : ess.getUserMap().matchUsersExcludingHidden(
				args[0], getPlayerOrNull(sender));

		for (IUser matchPlayer : matches)
		{
			if (isExempt(sender, matchPlayer))
			{
				informSender(sender, false, matchPlayer);
				continue;
			}

			playerMatch(matchPlayer, args[1]);
			informPlayer(matchPlayer);
			informSender(sender, true, matchPlayer);
		}
	}

}
