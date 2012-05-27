package com.earth2me.essentials.commands;

import static com.earth2me.essentials.I18n._;
import com.earth2me.essentials.utils.Util;
import com.earth2me.essentials.api.IUser;
import com.earth2me.essentials.permissions.Permissions;
import org.bukkit.command.CommandSender;


public class Commandbalance extends EssentialsCommand
{
	@Override
	protected void run(final CommandSender sender, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 1)
		{
			throw new NotEnoughArgumentsException();
		}
		sender.sendMessage(_("balance", Util.displayCurrency(getPlayer(server, args, 0, true).getMoney(), ess)));
	}

	@Override
	public void run(final IUser user, final String commandLabel, final String[] args) throws Exception
	{
		final double bal = (args.length < 1
							|| !Permissions.BALANCE_OTHERS.isAuthorized(user)
							? user
							: getPlayer(server, args, 0, true)).getMoney();
		user.sendMessage(_("balance", Util.displayCurrency(bal, ess)));
	}
}
