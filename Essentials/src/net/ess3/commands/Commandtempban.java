package net.ess3.commands;

import net.ess3.Console;
import static net.ess3.I18n._;
import net.ess3.api.IUser;
import net.ess3.permissions.Permissions;
import net.ess3.user.Ban;
import net.ess3.user.UserData;
import net.ess3.utils.DateUtil;
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
		final IUser user = ess.getUserMap().matchUser(args[0], true);
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
		UserData userData = user.getData();
		userData.setBan(new Ban());
		Ban ban = userData.getBan();
		ban.setReason(banReason);
		ban.setTimeout(banTimestamp);
		user.setBanned(true);
		user.queueSave();
		user.getPlayer().kickPlayer(banReason);
		final String senderName = isUser(sender) ? getPlayer(sender).getDisplayName() : Console.NAME;

		for (Player onlinePlayer : server.getOnlinePlayers())
		{
			final IUser player = ess.getUserMap().getUser(onlinePlayer);
			if (Permissions.BAN_NOTIFY.isAuthorized(player))
			{
				onlinePlayer.sendMessage(_("playerBanned", senderName, user.getName(), banReason));
			}
		}
	}
}
