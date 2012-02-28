package com.earth2me.essentials.components.commands.handlers;

import com.earth2me.essentials.Console;
import static com.earth2me.essentials.components.i18n.I18nComponent._;
import com.earth2me.essentials.Util;
import com.earth2me.essentials.api.IReplyTo;
import com.earth2me.essentials.components.commands.EssentialsCommand;
import com.earth2me.essentials.components.commands.NotEnoughArgumentsException;
import com.earth2me.essentials.components.settings.users.IUserComponent;
import com.earth2me.essentials.perm.Permissions;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class Commandr extends EssentialsCommand
{
	@Override
	public void run(final CommandSender sender, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 1)
		{
			throw new NotEnoughArgumentsException();
		}

		String message = getFinalArg(args, 0);
		IReplyTo replyTo;
		String senderName;

		if (sender instanceof Player)
		{
			IUserComponent user = getContext().getUser((Player)sender);
			if (Permissions.MSG_COLOR.isAuthorized(user))
			{
				message = Util.replaceColor(message);
			}
			else
			{
				message = Util.stripColor(message);
			}
			replyTo = user;
			senderName = user.getDisplayName();
		}
		else
		{
			message = Util.replaceColor(message);
			replyTo = Console.getConsoleReplyTo();
			senderName = Console.NAME;
		}

		final CommandSender target = replyTo.getReplyTo();
		final String targetName = target instanceof Player ? ((Player)target).getDisplayName() : Console.NAME;

		if (target == null || ((target instanceof Player) && !((Player)target).isOnline()))
		{
			throw new Exception(_("foreverAlone"));
		}

		sender.sendMessage(_("msgFormat", _("me"), targetName, message));
		if (target instanceof Player)
		{
			IUserComponent player = getContext().getUser((Player)target);
			if (player.isIgnoringPlayer(sender instanceof Player ? ((Player)sender).getName() : Console.NAME))
			{
				return;
			}
		}
		target.sendMessage(_("msgFormat", senderName, _("me"), message));
		replyTo.setReplyTo(target);
		if (target != sender)
		{
			if (target instanceof Player)
			{
				getContext().getUser((Player)target).setReplyTo(sender);
			}
			else
			{
				Console.getConsoleReplyTo().setReplyTo(sender);
			}
		}
	}

	@Override
	public String getPermission()
	{
		return "essentials.msg";
	}
}
