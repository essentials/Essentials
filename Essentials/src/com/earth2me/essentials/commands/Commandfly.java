package com.earth2me.essentials.commands;

import static com.earth2me.essentials.I18n._;
import com.earth2me.essentials.User;
import java.util.List;
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

	private void flyOtherPlayers(final Server server, final CommandSender sender, final String[] args) throws NotEnoughArgumentsException
	{
		User target = getPlayer(server, args, 0, (!sender instanceof player || sender.isAuthorized("essentials.vanish.interact")), false);
		if (args.length > 1)
		{
			if (args[1].contains("on") || args[1].contains("ena") || args[1].equalsIgnoreCase("1"))
			{
				target.setAllowFlight(true);
			}
			else
			{
				target.setAllowFlight(false);
			}
		}
		else
		{
			target.setAllowFlight(!player.getAllowFlight());
		}

		if (!target.getAllowFlight())
		{
			target.setFlying(false);
		}
		sender.sendMessage(_("flyMode", _(target.getAllowFlight() ? "enabled" : "disabled"), target.getDisplayName()));
	}
}
