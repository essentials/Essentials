package com.earth2me.essentials.commands;

import com.earth2me.essentials.Console;
import static com.earth2me.essentials.I18n._;
import com.earth2me.essentials.api.IUser;
import org.bukkit.Server;
import com.earth2me.essentials.permissions.Permissions;
import com.earth2me.essentials.user.Ban;
import lombok.Cleanup;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class Commandban extends EssentialsCommand
{
	@Override
	public void run(final CommandSender sender, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 1)
		{
			throw new NotEnoughArgumentsException();
		}
		@Cleanup
		final IUser user = getPlayer(args, 0, true);
		if (!user.isOnline())
		{
			if (sender instanceof Player && Permissions.BAN_OFFLINE.isAuthorized(user))
			{
				sender.sendMessage(_("banExempt"));
				return;
			}
		}
		else
		{
			if (Permissions.BAN_EXEMPT.isAuthorized(sender))
			{
				sender.sendMessage(_("banExempt"));
				return;
			}
		}

		user.acquireWriteLock();
		final String senderName = sender instanceof Player ? ((Player)sender).getDisplayName() : Console.NAME;
		String banReason;
		user.getData().setBan(new Ban());
		if (args.length > 1)
		{
			
			banReason = _("banFormat", getFinalArg(args, 1), senderName);
			user.getData().getBan().setReason(banReason);
		}
		else
		{
			banReason = _("banFormat", _("defaultBanReason"), senderName);
			user.getData().getBan().setReason("");
		}
		
		user.setBanned(true);
		user.kickPlayer(banReason);
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
