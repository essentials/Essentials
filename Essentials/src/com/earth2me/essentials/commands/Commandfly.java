package com.earth2me.essentials.commands;

import static com.earth2me.essentials.I18n._;
import com.earth2me.essentials.api.IUser;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class Commandfly extends EssentialsCommand
{
	@Override
	protected void run(final CommandSender sender, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 1)
		{
			throw new NotEnoughArgumentsException();
		}

		flyOtherPlayers(server, sender, args[0]);
	}

	@Override
	protected void run(final IUser user, final String commandLabel, final String[] args) throws Exception
	{
		//todo permissions
		if (args.length > 0 && args[0].trim().length() > 2 && user.isAuthorized("essentials.fly.others"))
		{
			flyOtherPlayers(server, user, args[0]);
			return;
		}
		user.setAllowFlight(!user.getAllowFlight());
		if (!user.getAllowFlight())
		{
			user.setFlying(false);
		}
		user.sendMessage(_("flyMode", _(user.getAllowFlight() ? "enabled" : "disabled"), user.getDisplayName()));
	}

	private void flyOtherPlayers(final Server server, final CommandSender sender, final String name)
	{
		for (Player matchPlayer : server.matchPlayer(name))
		{
			final IUser player = ess.getUser(matchPlayer);
			if (player.isHidden())
			{
				continue;
			}
			player.setAllowFlight(!player.getAllowFlight());
			if (!player.getAllowFlight())
			{
				player.setFlying(false);
			}
			sender.sendMessage(_("flyMode", _(player.getAllowFlight() ? "enabled" : "disabled"), player.getDisplayName()));
		}
	}
}
