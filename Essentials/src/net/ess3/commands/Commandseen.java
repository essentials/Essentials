package net.ess3.commands;

import lombok.Cleanup;
import static net.ess3.I18n._;
import net.ess3.api.IUser;
import net.ess3.permissions.Permissions;
import net.ess3.user.PlayerNotFoundException;
import net.ess3.user.UserData.TimestampType;
import net.ess3.utils.DateUtil;
import org.bukkit.command.CommandSender;

public class Commandseen extends EssentialsCommand
{
	@Override
	protected void run(final CommandSender sender, final String commandLabel, final String[] args) throws Exception
	{
		seen(sender,args,true);
	}
	
	@Override
	protected void run(final IUser user, final String commandLabel, final String[] args) throws Exception
	{
		seen(user,args,Permissions.SEEN_BANREASON.isAuthorized(user));
	}
	
	protected void seen (final CommandSender sender, final String[] args, final boolean show) throws Exception
	{
		if (args.length < 1)
		{
			throw new NotEnoughArgumentsException();
		}
		try
		{
			IUser u = ess.getUserMap().matchUser(args[0], false, false);
			sender.sendMessage(_("seenOnline", u.getPlayer().getDisplayName(), DateUtil.formatDateDiff(u.getTimestamp(TimestampType.LOGIN))));
		}
		catch (PlayerNotFoundException e)
		{
			@Cleanup
			IUser u = ess.getUserMap().getUser(args[0]);
			if (u == null)
			{
				throw new Exception(_("playerNotFound"));
			}
			u.acquireReadLock();
			sender.sendMessage(_("seenOffline", u.getPlayer().getDisplayName(), DateUtil.formatDateDiff(u.getTimestamp(TimestampType.LOGOUT))));
			if (u.isBanned())
			{
				sender.sendMessage(_("whoisBanned", show ? u.getData().getBan().getReason() : _("true")));
			}
		}
	}
}
