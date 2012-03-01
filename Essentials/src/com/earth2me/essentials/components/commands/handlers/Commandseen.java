package com.earth2me.essentials.components.commands.handlers;

import com.earth2me.essentials.Util;
import com.earth2me.essentials.components.commands.EssentialsCommand;
import com.earth2me.essentials.components.commands.NotEnoughArgumentsException;
import static com.earth2me.essentials.components.i18n.I18nComponent.$;
import com.earth2me.essentials.components.users.IUserComponent;
import com.earth2me.essentials.components.users.TimeStampType;
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
			IUserComponent u = getPlayer(args, 0);
			sender.sendMessage($("seenOnline", u.getDisplayName(), Util.formatDateDiff(u.getTimeStamp(TimeStampType.LOGIN))));
		}
		catch (NoSuchFieldException e)
		{
			IUserComponent u = getContext().getUser(args[0]);
			if (u == null)
			{
				throw new Exception($("playerNotFound"));
			}
			sender.sendMessage($("seenOffline", u.getDisplayName(), Util.formatDateDiff(u.getTimeStamp(TimeStampType.LOGOUT))));
			if (u.isBanned())
			{
				sender.sendMessage($("whoisBanned", $("true")));
			}
		}
	}
}
