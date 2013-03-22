package com.earth2me.essentials.commands;

import static com.earth2me.essentials.I18n._;
import com.earth2me.essentials.User;
import java.util.List;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class Commandtptoggle extends EssentialsCommand
{
	public Commandtptoggle()
	{
		super("tptoggle");
	}

	@Override
	public void run(final Server server, final CommandSender sender, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 1)
		{
			throw new NotEnoughArgumentsException();
		}

		toggleOtherPlayers(server, sender, args);
	}

	@Override
	public void run(final Server server, final User user, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length > 0 && args[0].trim().length() > 2 && user.isAuthorized("essentials.tptoggle.others"))
		{
			toggleOtherPlayers(server, user, args);
			return;
		}

		user.sendMessage(user.toggleTeleportEnabled() ? _("teleportationEnabled") : _("teleportationDisabled"));
	}

	private void toggleOtherPlayers(final Server server, final CommandSender sender, final String[] args) throws NotEnoughArgumentsException
	{
		final User target = getPlayer(server, sender, args, 0);
		if (args.length > 1)
		{
			if (args[1].contains("on") || args[1].contains("ena") || args[1].equalsIgnoreCase("1"))
			{
				target.setTeleportEnabled(true);
			}
			else
			{
				target.setTeleportEnabled(false);
			}
		}
		else
		{
			target.toggleTeleportEnabled();
		}

		final boolean enabled = target.isTeleportEnabled();


		target.sendMessage(enabled ? _("teleportationEnabled") : _("teleportationDisabled"));
		sender.sendMessage(enabled ? _("teleportationEnabledFor", target.getDisplayName()) : _("teleportationDisabledFor", target.getDisplayName()));
	}
}
