package com.earth2me.essentials.commands;

import static com.earth2me.essentials.I18n._;
import com.earth2me.essentials.utils.Util;
import com.earth2me.essentials.api.IUser;
import com.earth2me.essentials.permissions.Permissions;
import com.earth2me.essentials.user.UserData.TimestampType;
import lombok.Cleanup;
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
			IUser u = getPlayer(args, 0);
			sender.sendMessage(_("seenOnline", u.getDisplayName(), DateUtil.formatDateDiff(u.getTimestamp(TimestampType.LOGIN))));
		}
		catch (NoSuchFieldException e)
		{
			@Cleanup
			IUser u = ess.getUser(args[0]);
			u.acquireReadLock();
			if (u == null)
			{
				throw new Exception(_("playerNotFound"));
			}
			sender.sendMessage(_("seenOffline", u.getDisplayName(), DateUtil.formatDateDiff(u.getTimestamp(TimestampType.LOGOUT))));
			if (u.isBanned())
			{
				sender.sendMessage(_("whoisBanned", show ? u.getData().getBan().getReason() : _("true")));
			}
		}
	}
}
