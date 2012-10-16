package net.ess3.commands;

import static net.ess3.I18n._;
import net.ess3.api.IUser;
import net.ess3.permissions.Permissions;
import org.bukkit.command.CommandSender;


public class Commandgod extends EssentialsCommand
{
	@Override
	protected void run(final CommandSender sender, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 1)
		{
			throw new NotEnoughArgumentsException();
		}

		godOtherPlayers(sender, args);
	}

	@Override
	protected void run(final IUser user, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length > 0 && !args[0].trim().isEmpty() && Permissions.GOD_OTHERS.isAuthorized(user))
		{
			godOtherPlayers(user, args);
			return;
		}

		user.setGodModeEnabled(!user.isGodModeEnabled());
		user.sendMessage(_("godMode", (user.isGodModeEnabled() ? _("enabled") : _("disabled"))));
	}

	private void godOtherPlayers(final CommandSender sender, final String[] args)
	{
		for (IUser player : ess.getUserMap().matchUsers(args[0], true))
		{
			if (player.isOnline()
				? Permissions.GOD_EXEMPT.isAuthorized(player)
				: !Permissions.GOD_OFFLINE.isAuthorized(sender))
			{
				sender.sendMessage("Can't change god mode for player " + player.getName()); //TODO: I18n
				continue;
			}
			if (args.length > 1)
			{
				if (args[1].contains("on") || args[1].contains("ena") || args[1].equalsIgnoreCase("1"))
				{
					player.setGodModeEnabled(true);
				}
				else
				{
					player.setGodModeEnabled(false);
				}
			}
			else
			{
				player.setGodModeEnabled(!player.isGodModeEnabled());
			}

			final boolean enabled = player.isGodModeEnabled();
			player.sendMessage(_("godMode", (enabled ? _("enabled") : _("disabled"))));
			sender.sendMessage(_("godMode", _(enabled ? "godEnabledFor" : "godDisabledFor", player.getName())));
		}
	}
}
