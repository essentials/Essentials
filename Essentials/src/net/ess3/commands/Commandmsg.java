package net.ess3.commands;

import java.util.Set;
import net.ess3.Console;
import static net.ess3.I18n._;
import net.ess3.api.IReplyTo;
import net.ess3.api.IUser;
import net.ess3.permissions.Permissions;
import net.ess3.utils.FormatUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


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
			final IUser user = getUser(sender);
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
		

		final Set<IUser> matchedPlayers = ess.getUserMap().matchUsers(args[0], false);

		if (matchedPlayers.isEmpty())
		{
			throw new Exception(_("playerNotFound"));
		}

		final Player player = getPlayerOrNull(sender);
		if (isUser(sender))
		{
			int i = 0;
			
			for (IUser u : matchedPlayers)
			{
				if (!player.canSee(u.getPlayer()))
				{
					i++;
				}
			}
			if (i == matchedPlayers.size())
			{
				throw new Exception(_("playerNotFound"));
			}
		}

		for (IUser matchedPlayer : matchedPlayers)
		{
			final Player realPlayer = matchedPlayer.getPlayer();
			sender.sendMessage(_("msgFormat", translatedMe, realPlayer.getDisplayName(), message));
			if (isUser(sender) && (matchedPlayer.isIgnoringPlayer(getUser(sender)) || !player.canSee(realPlayer)))
			{
				continue;
			}
			matchedPlayer.sendMessage(_("msgFormat", senderName, translatedMe, message));
			replyTo.setReplyTo(matchedPlayer);
			matchedPlayer.setReplyTo(sender);
		}
	}
}
