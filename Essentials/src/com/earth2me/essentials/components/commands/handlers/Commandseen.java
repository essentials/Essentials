package com.earth2me.essentials.components.commands.handlers;

import static com.earth2me.essentials.I18nComponent._;
import com.earth2me.essentials.Util;
import com.earth2me.essentials.components.commands.EssentialsCommand;
import com.earth2me.essentials.components.commands.NotEnoughArgumentsException;
import com.earth2me.essentials.components.users.IUser;
import com.earth2me.essentials.components.users.UserData.TimestampType;
import org.bukkit.command.CommandSender;


public class Commandseen extends EssentialsCommand
{
	@Override
	protected void run(final CommandSender sender, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 1)
		{
			throw new NotEnoughArgumentsException();
		}
		try
		{
			IUser u = getPlayer(args, 0);
			sender.sendMessage(_("seenOnline", u.getDisplayName(), Util.formatDateDiff(u.getTimestamp(TimestampType.LOGIN))));
		}
		catch (NoSuchFieldException e)
		{
			IUser u = getContext().getUser(args[0]);
			if (u == null)
			{
				throw new Exception(_("playerNotFound"));
			}
			sender.sendMessage(_("seenOffline", u.getDisplayName(), Util.formatDateDiff(u.getTimestamp(TimestampType.LOGOUT))));
			if (u.isBanned())
			{
				sender.sendMessage(_("whoisBanned", _("true")));
			}
		}
	}
}
