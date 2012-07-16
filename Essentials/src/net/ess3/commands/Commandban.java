package net.ess3.commands;

import net.ess3.Console;
import static net.ess3.I18n._;
import net.ess3.api.IUser;
import net.ess3.permissions.Permissions;
import net.ess3.user.Ban;
import lombok.Cleanup;


public class Commandban extends EssentialsCommand
{
	@Override
	protected void run(final CommandSender sender, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 1)
		{
			throw new NotEnoughArgumentsException();
		}
		@Cleanup
		final IUser user = getPlayer(args, 0, true);
		if (!user.isOnline())
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

		for (IPlayer onlinePlayer : server.getOnlinePlayers())
		{
			final IUser player = onlinePlayer.getUser();
			if (Permissions.BAN_NOTIFY.isAuthorized(player))
			{
				onlinePlayer.sendMessage(_("playerBanned", senderName, user.getName(), banReason));
			}
		}
	}
}
