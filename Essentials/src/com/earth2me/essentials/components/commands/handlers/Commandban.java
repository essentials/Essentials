package com.earth2me.essentials.components.commands.handlers;

import com.earth2me.essentials.components.messenger.Console;
import com.earth2me.essentials.components.commands.EssentialsCommand;
import com.earth2me.essentials.components.commands.NotEnoughArgumentsException;
import static com.earth2me.essentials.components.i18n.I18nComponent._;
import com.earth2me.essentials.components.users.Ban;
import com.earth2me.essentials.components.users.IUserComponent;
import com.earth2me.essentials.perm.Permissions;
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
		final IUserComponent user = getPlayer(args, 0, true);
		if (user.isOnline())
		{
			if (Permissions.BAN_EXEMPT.isAuthorized(user))
			{
				sender.sendMessage(_("banExempt"));
				return;
			}
		}
		else
		{
			if (Permissions.BAN_OFFLINE.isAuthorized(sender))
			{
				sender.sendMessage(_("banExempt"));
				return;
			}
		}

		user.acquireWriteLock();
		String banReason;
		user.getData().setBan(new Ban());
		if (args.length > 1)
		{
			banReason = getFinalArg(args, 1);
			user.getData().getBan().setReason(banReason);
		}
		else
		{
			banReason = _("defaultBanReason");
		}
		user.setBanned(true);
		user.kickPlayer(banReason);
		final String senderName = sender instanceof Player ? ((Player)sender).getDisplayName() : Console.NAME;

		for (Player onlinePlayer : getServer().getOnlinePlayers())
		{
			final IUserComponent player = getContext().getUser(onlinePlayer);
			if (Permissions.BAN_NOTIFY.isAuthorized(player))
			{
				onlinePlayer.sendMessage(_("playerBanned", senderName, user.getName(), banReason));
			}
		}
	}
}
