package org.mcess.essentials.commands;

import org.mcess.essentials.CommandSource;
import org.mcess.essentials.Console;
import org.mcess.essentials.User;
import org.mcess.essentials.utils.DateUtil;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import org.bukkit.BanList;
import org.bukkit.Server;
import org.mcess.essentials.I18n;


public class Commandtempban extends EssentialsCommand
{
	public Commandtempban()
	{
		super("tempban");
	}

	@Override
	public void run(final Server server, final CommandSource sender, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 2)
		{
			throw new NotEnoughArgumentsException();
		}
		final User user = getPlayer(server, args, 0, true, true);
		if (!user.getBase().isOnline())
		{
			if (sender.isPlayer()
				&& !ess.getUser(sender.getPlayer()).isAuthorized("essentials.tempban.offline"))
			{
				sender.sendMessage(I18n.tl("tempbanExemptOffline"));
				return;
			}
		}
		else
		{
			if (user.isAuthorized("essentials.tempban.exempt") && sender.isPlayer())
			{
				sender.sendMessage(I18n.tl("tempbanExempt"));
				return;
			}
		}
		final String time = getFinalArg(args, 1);
		final long banTimestamp = DateUtil.parseDateDiff(time, true);
		String banReason = DateUtil.removeTimePattern(time);

		final long maxBanLength = ess.getSettings().getMaxTempban() * 1000;
		if (maxBanLength > 0 && ((banTimestamp - GregorianCalendar.getInstance().getTimeInMillis()) > maxBanLength)
			&& sender.isPlayer() && !(ess.getUser(sender.getPlayer()).isAuthorized("essentials.tempban.unlimited")))
		{
			sender.sendMessage(I18n.tl("oversizedTempban"));
			throw new NoChargeException();
		}

		if (banReason.length() < 2)
		{
			banReason = I18n.tl("defaultBanReason");
		}

		final String senderName = sender.isPlayer() ? sender.getPlayer().getDisplayName() : Console.NAME;
		ess.getServer().getBanList(BanList.Type.NAME).addBan(user.getName(), banReason, new Date(banTimestamp), senderName);
		final String expiry = DateUtil.formatDateDiff(banTimestamp);

		final String banDisplay = I18n.tl("tempBanned", expiry, senderName, banReason);
		user.getBase().kickPlayer(banDisplay);

		final String message = I18n.tl("playerTempBanned", senderName, user.getName(), expiry, banReason);
		server.getLogger().log(Level.INFO, message);
		ess.broadcastMessage("essentials.ban.notify", message);
	}
}
