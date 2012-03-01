package com.earth2me.essentials.components.commands.handlers;

import com.earth2me.essentials.Util;
import com.earth2me.essentials.components.commands.EssentialsCommand;
import com.earth2me.essentials.components.commands.NotEnoughArgumentsException;
import static com.earth2me.essentials.components.i18n.I18nComponent.$;
import com.earth2me.essentials.components.users.IUserComponent;
import com.earth2me.essentials.perm.Permissions;
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
		sender.sendMessage($("balance", Util.formatCurrency(getPlayer(args, 0, true).getMoney(), getContext())));
	}

	@Override
	public void run(final IUserComponent user, final String commandLabel, final String[] args) throws Exception
	{
		final double bal = (args.length < 1
							|| !Permissions.BALANCE_OTHERS.isAuthorized(user)
							? user
							: getPlayer(args, 0, true)).getMoney();
		user.sendMessage($("balance", Util.formatCurrency(bal, getContext())));
	}
}
