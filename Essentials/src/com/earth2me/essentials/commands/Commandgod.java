package com.earth2me.essentials.commands;

import static com.earth2me.essentials.I18n._;
import com.earth2me.essentials.api.IUser;
import com.earth2me.essentials.permissions.Permissions;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


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
		for (Player matchPlayer : server.matchPlayer(args[0]))
		{
			final IUser player = ess.getUser(matchPlayer);
			if (player.isHidden())
			{
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
			sender.sendMessage(_("godMode", _(enabled ? "godEnabledFor" : "godDisabledFor", matchPlayer.getDisplayName())));
		}
	}
}
