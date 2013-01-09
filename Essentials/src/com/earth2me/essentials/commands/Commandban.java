package com.earth2me.essentials.commands;

import com.earth2me.essentials.Console;
import static com.earth2me.essentials.I18n._;
import com.earth2me.essentials.OfflinePlayer;
import com.earth2me.essentials.User;
import com.earth2me.essentials.Util;
import java.util.logging.Level;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class Commandban extends EssentialsCommand
{
	public Commandban()
	{
		super("ban");
	}

	@Override
	public void run(final Server server, final CommandSender sender, final String commandLabel, final String[] args) throws Exception
	{
		boolean nomatch = false;
		if (args.length < 1)
		{
			throw new NotEnoughArgumentsException();
		}
		User user;
		try
		{
			user = getPlayer(server, args, 0, true);
		}
		catch (NoSuchFieldException e)
		{
			nomatch = true;
			user = ess.getUser(new OfflinePlayer(args[0], ess));
		}
		if (!user.isOnline())
		{
			if (sender instanceof Player
				&& !ess.getUser(sender).isAuthorized("essentials.ban.offline"))
			{
				sender.sendMessage(_("banExempt"));
				return;
			}
		}
		else
		{
			if (user.isAuthorized("essentials.ban.exempt") && sender instanceof Player)
			{
				sender.sendMessage(_("banExempt"));
				return;
			}
		}

		final String senderName = sender instanceof Player ? ((Player)sender).getDisplayName() : Console.NAME;
		String banReason;
		if (args.length > 1)
		{
			banReason = _("banFormat", Util.replaceFormat(getFinalArg(args, 1).replace("\\n", "\n")), senderName);
		}
		else
		{
			banReason = _("banFormat", _("defaultBanReason"), senderName);
		}

		user.setBanReason(banReason);
		user.setBanned(true);
		user.setBanTimeout(0);
		user.kickPlayer(banReason);
		
		server.getLogger().log(Level.INFO, _("playerBanned", senderName, user.getName(), banReason));
		
		if (nomatch) {
			sender.sendMessage(_("userUnknown", args[0]));
		}

		for (Player onlinePlayer : server.getOnlinePlayers())
		{
			final User player = ess.getUser(onlinePlayer);
			if (onlinePlayer == sender || player.isAuthorized("essentials.ban.notify"))
			{
				onlinePlayer.sendMessage(_("playerBanned", senderName, user.getName(), banReason));
			}
		}
	}
}
