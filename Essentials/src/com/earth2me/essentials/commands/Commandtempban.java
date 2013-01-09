package com.earth2me.essentials.commands;

import com.earth2me.essentials.Console;
import static com.earth2me.essentials.I18n._;
import com.earth2me.essentials.User;
import com.earth2me.essentials.Util;
import java.util.GregorianCalendar;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class Commandtempban extends EssentialsCommand
{
	public Commandtempban()
	{
		super("tempban");
	}

	@Override
	public void run(final Server server, final CommandSender sender, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 2)
		{
			throw new NotEnoughArgumentsException();
		}
		final User user = getPlayer(server, args, 0, true);
		if (!user.isOnline())
		{
			if (sender instanceof Player
				&& !ess.getUser(sender).isAuthorized("essentials.tempban.offline"))
			{
				sender.sendMessage(_("tempbanExempt"));
				return;
			}
		}
		else
		{
			if (user.isAuthorized("essentials.tempban.exempt") && sender instanceof Player)
			{
				sender.sendMessage(_("tempbanExempt"));
				return;
			}
		}
		final String time = getFinalArg(args, 1);
		final long banTimestamp = Util.parseDateDiff(time, true);

		final long maxBanLength = ess.getSettings().getMaxTempban() * 1000;
		if (maxBanLength > 0 && ((banTimestamp - GregorianCalendar.getInstance().getTimeInMillis()) > maxBanLength) && !(ess.getUser(sender).isAuthorized("essentials.tempban.unlimited")))
		{
			sender.sendMessage(_("oversizedTempban"));
			throw new NoChargeException();
		}

		final String senderName = sender instanceof Player ? ((Player)sender).getDisplayName() : Console.NAME;
		final String banReason = _("tempBanned", Util.formatDateDiff(banTimestamp), senderName);
		user.setBanReason(banReason);
		user.setBanTimeout(banTimestamp);
		user.setBanned(true);
		user.kickPlayer(banReason);

		for (Player onlinePlayer : server.getOnlinePlayers())
		{
			final User player = ess.getUser(onlinePlayer);
			if (player.isAuthorized("essentials.ban.notify"))
			{
				onlinePlayer.sendMessage(_("playerBanned", senderName, user.getName(), banReason));
			}
		}
	}
}
