package com.earth2me.essentials.commands;

import com.earth2me.essentials.Console;
import static com.earth2me.essentials.I18n._;
import com.earth2me.essentials.IReplyTo;
import com.earth2me.essentials.User;
import com.earth2me.essentials.utils.FormatUtil;
import java.util.List;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class Commandmsg extends EssentialsCommand
{
	public Commandmsg()
	{
		super("msg");
	}

	@Override
	public void run(Server server, CommandSender sender, String commandLabel, String[] args) throws Exception
	{
		if (args.length < 2 || args[0].trim().length() < 2 || args[1].trim().isEmpty())
		{
			throw new NotEnoughArgumentsException();
		}

		String message = getFinalArg(args, 1);
		if (sender instanceof Player)
		{
			User user = ess.getUser(sender);
			if (user.isMuted())
			{
				throw new Exception(_("voiceSilenced"));
			}
			message = FormatUtil.formatMessage(user, "essentials.msg", message);
		}
		else
		{
			message = FormatUtil.replaceFormat(message);
		}

		final String translatedMe = _("me");

		final IReplyTo replyTo = sender instanceof Player ? ess.getUser((Player)sender) : Console.getConsoleReplyTo();
		final String senderName = sender instanceof Player ? ((Player)sender).getDisplayName() : Console.NAME;

		if (args[0].equalsIgnoreCase(Console.NAME))
		{
			sender.sendMessage(_("msgFormat", translatedMe, Console.NAME, message));
			CommandSender cs = Console.getCommandSender(server);
			cs.sendMessage(_("msgFormat", senderName, translatedMe, message));
			replyTo.setReplyTo(cs);
			Console.getConsoleReplyTo().setReplyTo(sender);
			return;
		}

		boolean skipHidden = sender instanceof Player && !ess.getUser(sender).isAuthorized("essentials.vanish.interact");
		boolean foundUser = false;
		final List<Player> matchedPlayers = server.matchPlayer(args[0]);

		for (Player matchPlayer : matchedPlayers)
		{
			final User matchedUser = ess.getUser(matchPlayer);

			if (skipHidden && matchedUser.isHidden())
			{
				continue;
			}
			foundUser = true;
			if (matchedUser.isAfk())
			{
				sender.sendMessage(_("userAFK", matchPlayer.getDisplayName()));
			}

			sender.sendMessage(_("msgFormat", translatedMe, matchPlayer.getDisplayName(), message));
			if (sender instanceof Player && matchedUser.isIgnoredPlayer(ess.getUser(sender)))
			{
				continue;
			}

			matchPlayer.sendMessage(_("msgFormat", senderName, translatedMe, message));
			replyTo.setReplyTo(matchedUser);
			ess.getUser(matchPlayer).setReplyTo(sender);
		}

		if (!foundUser)
		{
			throw new Exception(_("playerNotFound"));
		}
	}
}
