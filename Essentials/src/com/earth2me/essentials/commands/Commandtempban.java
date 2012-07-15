package com.earth2me.essentials.commands;

import com.earth2me.essentials.Console;
import static com.earth2me.essentials.I18n._;
import com.earth2me.essentials.utils.Util;
import com.earth2me.essentials.api.IUser;
import com.earth2me.essentials.api.server.CommandSender;
import com.earth2me.essentials.permissions.Permissions;
import com.earth2me.essentials.user.Ban;
import com.earth2me.essentials.utils.DateUtil;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class Commandtempban extends EssentialsCommand
{
	@Override
	protected void run(final CommandSender sender, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 2)
		{
			throw new NotEnoughArgumentsException();
		}
		final IUser user = getPlayer(args, 0, true);
		if (!user.isOnline())
		{
			if (Permissions.TEMPBAN_OFFLINE.isAuthorized(sender))
			{
				sender.sendMessage(_("tempbanExempt"));
				return;
			}
		}
		else
		{
			if (Permissions.TEMPBAN_EXEMPT.isAuthorized(user))
			{
				sender.sendMessage(_("tempbanExempt"));
				return;
			}
		}
		final String time = getFinalArg(args, 1);
		final long banTimestamp = DateUtil.parseDateDiff(time, true);

		final String banReason = _("tempBanned", DateUtil.formatDateDiff(banTimestamp));
		user.acquireWriteLock();
		user.getData().setBan(new Ban());
		user.getData().getBan().setReason(banReason);
		user.getData().getBan().setTimeout(banTimestamp);
		user.setBanned(true);
		user.kickPlayer(banReason);
		final String senderName = sender instanceof Player ? ((Player)sender).getDisplayName() : Console.NAME;

		for (Player onlinePlayer : server.getOnlinePlayers())
		{
			final IUser player = ess.getUser(onlinePlayer);
			if (Permissions.BAN_NOTIFY.isAuthorized(player))
			{
				onlinePlayer.sendMessage(_("playerBanned", senderName, user.getName(), banReason));
			}
		}
	}
}
