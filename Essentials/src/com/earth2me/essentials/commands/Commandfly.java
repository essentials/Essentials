package com.earth2me.essentials.commands;

import static com.earth2me.essentials.I18n._;
import com.earth2me.essentials.User;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class Commandfly extends EssentialsCommand
{
	public Commandfly()
	{
		super("fly");
	}

	@Override
	protected void run(final Server server, final CommandSender sender, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 1)
		{
			throw new NotEnoughArgumentsException();
		}

		flyOtherPlayers(server, sender, args);
	}

	@Override
	protected void run(final Server server, final User user, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length == 1)
		{
			if (args[0].equalsIgnoreCase("on") || args[0].startsWith("ena") || args[0].equalsIgnoreCase("1"))
			{
				user.setAllowFlight(true);
			}
			else if (args[0].equalsIgnoreCase("off") || args[0].startsWith("dis") || args[0].equalsIgnoreCase("0"))
			{
				user.setAllowFlight(false);
			}
			else if (user.isAuthorized("essentials.fly.others"))
			{
				flyOtherPlayers(server, user, args);
				return;
			}
		}
		else if (args.length == 2 && user.isAuthorized("essentials.fly.others"))
		{
			flyOtherPlayers(server, user, args);
			return;
		}
		else
		{
			user.setAllowFlight(!user.getAllowFlight());
			if (!user.getAllowFlight())
			{
				user.setFlying(false);
			}
		}
		user.sendMessage(_("flyMode", _(user.getAllowFlight() ? "enabled" : "disabled"), user.getDisplayName()));
	}

	private void flyOtherPlayers(final Server server, final CommandSender sender, final String[] args)
	{
		for (Player matchPlayer : server.matchPlayer(args[0]))
		{
			final User player = ess.getUser(matchPlayer);
			if (player.isHidden())
			{
				continue;
			}

			if (args.length > 1)
			{
				if (args[1].contains("on") || args[1].contains("ena") || args[1].equalsIgnoreCase("1"))
				{
					player.setAllowFlight(true);
				}
				else
				{
					player.setAllowFlight(false);
				}
			}
			else
			{
				player.setAllowFlight(!player.getAllowFlight());
			}

			if (!player.getAllowFlight())
			{
				player.setFlying(false);
			}
			sender.sendMessage(_("flyMode", _(player.getAllowFlight() ? "enabled" : "disabled"), player.getDisplayName()));
		}
	}
}
