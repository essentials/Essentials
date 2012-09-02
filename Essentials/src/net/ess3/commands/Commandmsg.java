package net.ess3.commands;

import java.util.List;
import java.util.Set;
import lombok.Cleanup;
import net.ess3.Console;
import static net.ess3.I18n._;
import net.ess3.api.IReplyTo;
import net.ess3.api.IUser;
import net.ess3.permissions.Permissions;
import net.ess3.utils.FormatUtil;
import net.ess3.utils.Util;
import org.bukkit.command.CommandSender;



public class Commandmsg extends EssentialsCommand
{
	@Override
	protected void run(final CommandSender sender, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 2 || args[0].trim().length() < 3 || args[1].trim().isEmpty())
		{
			throw new NotEnoughArgumentsException();
		}

		String message = getFinalArg(args, 1);
		if (isUser(sender))
		{
			@Cleanup
			IUser user = getUser(sender);
			user.acquireReadLock();
			if (user.getData().isMuted())
			{
				throw new Exception(_("voiceSilenced"));
			}
			if (Permissions.MSG_COLOR.isAuthorized(user))
			{
				message = FormatUtil.replaceFormat(message);
			}
			else
			{
				message = FormatUtil.stripColor(message);
			}
		}
		else
		{
			message = FormatUtil.replaceFormat(message);
		}

		final String translatedMe = _("me");

		final IReplyTo replyTo = isUser(sender) ? getUser(sender) : Console.getConsoleReplyTo();
		final String senderName = isUser(sender) ? getPlayer(sender).getDisplayName() : Console.NAME;

		if (args[0].equalsIgnoreCase(Console.NAME))
		{
			sender.sendMessage(_("msgFormat", translatedMe, Console.NAME, message));
			CommandSender cs = server.getConsoleSender();
			cs.sendMessage(_("msgFormat", senderName, translatedMe, message));
			replyTo.setReplyTo(cs);
			Console.getConsoleReplyTo().setReplyTo(sender);
			return;
		}

		final Set<IUser> matchedPlayers = ess.getUserMap().matchUsers(args[0], true, false);

		if (matchedPlayers.isEmpty())
		{
			throw new Exception(_("playerNotFound"));
		}

		int i = 0;
		for (IUser u : matchedPlayers)
		{
			if (u.isHidden())
			{
				i++;
			}
		}
		if (i == matchedPlayers.size())
		{
			throw new Exception(_("playerNotFound"));
		}

		for (IUser matchedPlayer : matchedPlayers)
		{
			sender.sendMessage(_("msgFormat", translatedMe, matchedPlayer.getPlayer().getDisplayName(), message));
			if (isUser(sender) && (matchedPlayer.isIgnoringPlayer(getUser(sender)) || matchedPlayer.isHidden()))
			{
				continue;
			}
			matchedPlayer.sendMessage(_("msgFormat", senderName, translatedMe, message));
			replyTo.setReplyTo(matchedPlayer);
			matchedPlayer.setReplyTo(sender);
		}
	}
}
