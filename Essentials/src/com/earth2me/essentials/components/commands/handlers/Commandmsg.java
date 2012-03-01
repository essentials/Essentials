package com.earth2me.essentials.components.commands.handlers;

import com.earth2me.essentials.Console;
import com.earth2me.essentials.Util;
import com.earth2me.essentials.components.commands.EssentialsCommand;
import com.earth2me.essentials.components.commands.NotEnoughArgumentsException;
import static com.earth2me.essentials.components.i18n.I18nComponent._;
import com.earth2me.essentials.components.users.IReplyTo;
import com.earth2me.essentials.components.users.IUserComponent;
import com.earth2me.essentials.perm.Permissions;
import java.util.List;
import lombok.Cleanup;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class Commandmsg extends EssentialsCommand
{
	@Override
	public void run(final CommandSender sender, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 2 || args[0].trim().isEmpty() || args[1].trim().isEmpty())
		{
			throw new NotEnoughArgumentsException();
		}

		String message = getFinalArg(args, 1);
		if (sender instanceof Player)
		{
			@Cleanup
			IUserComponent user = getContext().getUser((Player)sender);
			user.acquireReadLock();
			if (user.getData().isMuted())
			{
				throw new Exception(_("voiceSilenced"));
			}
			if (Permissions.MSG_COLOR.isAuthorized(user))
			{
				message = Util.replaceColor(message);
			}
			else
			{
				message = Util.stripColor(message);
			}
		}
		else
		{
			message = Util.replaceColor(message);
		}

		final String translatedMe = _("me");

		final IReplyTo replyTo = sender instanceof Player ? getContext().getUser((Player)sender) : Console.getConsoleReplyTo();
		final String senderName = sender instanceof Player ? ((Player)sender).getDisplayName() : Console.NAME;

		if (args[0].equalsIgnoreCase(Console.NAME))
		{
			sender.sendMessage(_("msgFormat", translatedMe, Console.NAME, message));
			CommandSender cs = getServer().getConsoleSender();
			cs.sendMessage(_("msgFormat", senderName, translatedMe, message));
			replyTo.setReplyTo(cs);
			Console.getConsoleReplyTo().setReplyTo(sender);
			return;
		}

		final List<Player> matchedPlayers = getServer().matchPlayer(args[0]);

		if (matchedPlayers.isEmpty())
		{
			throw new Exception(_("playerNotFound"));
		}

		int i = 0;
		for (Player matchedPlayer : matchedPlayers)
		{
			final IUserComponent u = getContext().getUser(matchedPlayer);
			if (u.isHidden())
			{
				i++;
			}
		}
		if (i == matchedPlayers.size())
		{
			throw new Exception(_("playerNotFound"));
		}

		for (Player matchedPlayer : matchedPlayers)
		{
			sender.sendMessage(_("msgFormat", translatedMe, matchedPlayer.getDisplayName(), message));
			final IUserComponent matchedUser = getContext().getUser(matchedPlayer);
			if (sender instanceof Player && (matchedUser.isIgnoringPlayer(((Player)sender).getName()) || matchedUser.isHidden()))
			{
				continue;
			}
			matchedPlayer.sendMessage(_("msgFormat", senderName, translatedMe, message));
			replyTo.setReplyTo(getContext().getUser(matchedPlayer));
			getContext().getUser(matchedPlayer).setReplyTo(sender);
		}
	}
}
