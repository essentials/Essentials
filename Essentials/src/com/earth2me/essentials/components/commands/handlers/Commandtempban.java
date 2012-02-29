package com.earth2me.essentials.components.commands.handlers;

import com.earth2me.essentials.components.users.IUserComponent;
import com.earth2me.essentials.Console;
import static com.earth2me.essentials.components.i18n.I18nComponent.$;
import com.earth2me.essentials.Util;
import com.earth2me.essentials.components.commands.EssentialsCommand;
import com.earth2me.essentials.components.commands.NotEnoughArgumentsException;
import com.earth2me.essentials.perm.Permissions;
import com.earth2me.essentials.components.users.Ban;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class Commandtempban extends EssentialsCommand
{
	@Override
	public void run(final CommandSender sender, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 2)
		{
			throw new NotEnoughArgumentsException();
		}
		final IUserComponent user = getPlayer(args, 0, true);
		if (user.getBase() instanceof OfflinePlayer)
		{
			if (Permissions.TEMPBAN_OFFLINE.isAuthorized(sender))
			{
				sender.sendMessage($("tempbanExempt"));
				return;
			}
		}
		else
		{
			if (Permissions.TEMPBAN_EXEMPT.isAuthorized(user))
			{
				sender.sendMessage($("tempbanExempt"));
				return;
			}
		}
		final String time = getFinalArg(args, 1);
		final long banTimestamp = Util.parseDateDiff(time, true);

		final String banReason = $("tempBanned", Util.formatDateDiff(banTimestamp));
		user.acquireWriteLock();
		user.getData().setBan(new Ban());
		user.getData().getBan().setReason(banReason);
		user.getData().getBan().setTimeout(banTimestamp);
		user.setBanned(true);
		user.kickPlayer(banReason);
		final String senderName = sender instanceof Player ? ((Player)sender).getDisplayName() : Console.NAME;

		for (Player onlinePlayer : getServer().getOnlinePlayers())
		{
			final IUserComponent player = getContext().getUser(onlinePlayer);
			if (Permissions.BAN_NOTIFY.isAuthorized(player))
			{
				onlinePlayer.sendMessage($("playerBanned", senderName, user.getName(), banReason));
			}
		}
	}
}
