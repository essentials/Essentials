package net.ess3.commands;

import static net.ess3.I18n._;

import net.ess3.api.IUser;
import net.ess3.permissions.Permissions;
import net.ess3.storage.Location;
import net.ess3.user.UserData.TimestampType;
import net.ess3.utils.DateUtil;
import lombok.Cleanup;
import org.bukkit.command.CommandSender;


public class Commandseen extends EssentialsCommand
{
	@Override
	protected void run(final CommandSender sender, final String commandLabel, final String[] args) throws Exception
	{
		seen(sender,args,true, true);
	}
	
	@Override
	protected void run(final IUser user, final String commandLabel, final String[] args) throws Exception
	{
		seen(user,args,Permissions.SEEN_BANREASON.isAuthorized(user), Permissions.SEEN_EXTRA.isAuthorized(user));
	}
	
	protected void seen (final CommandSender sender, final String[] args, final boolean showBan, final boolean extra) throws Exception
	{
		if (args.length < 1)
		{
			throw new NotEnoughArgumentsException();
		}
		try
		{
			IUser player = getPlayer(args, 0);
			player.setDisplayNick();
			sender.sendMessage(_("seenOnline", player.getDisplayName(), DateUtil.formatDateDiff(player.getTimestamp(TimestampType.LOGIN))));
			if (extra)
			{
				sender.sendMessage(_("whoisIPAddress", player.getAddress().getAddress().toString()));
			}
		}
		catch (NoSuchFieldException e)
		{
			@Cleanup
			IUser player = ess.getUser(args[0]);
			player.acquireReadLock();
			if (player == null)
			{
				throw new Exception(_("playerNotFound"));
			}
			player.setDisplayNick();
			sender.sendMessage(_("seenOffline", player.getName(), DateUtil.formatDateDiff(player.getTimestamp(TimestampType.LOGOUT))));
			if (player.isBanned())
			{
				sender.sendMessage(_("whoisBanned", showBan ? player.getData().getBan().getReason() : _("true")));
			}
			if (extra)
			{
				sender.sendMessage(_("whoisIPAddress", player.getData().getIpAddress()));
				final Location loc = player.getData().getLastLocation();
				if (loc != null) {
					sender.sendMessage(_("whoisLocation", loc.getWorldName(), loc.getX(), loc.getY(), loc.getZ()));
				}
			}
		}
	}
}
