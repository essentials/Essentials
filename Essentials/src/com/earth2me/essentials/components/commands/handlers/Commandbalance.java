package com.earth2me.essentials.components.commands.handlers;

import static com.earth2me.essentials.components.i18n.I18nComponent._;
import com.earth2me.essentials.Util;
import com.earth2me.essentials.components.commands.EssentialsCommand;
import com.earth2me.essentials.components.commands.NotEnoughArgumentsException;
import com.earth2me.essentials.components.users.IUser;
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
		sender.sendMessage(_("balance", Util.formatCurrency(getPlayer(args, 0, true).getMoney(), getContext())));
	}

	@Override
	public void run(final IUser user, final String commandLabel, final String[] args) throws Exception
	{
		final double bal = (args.length < 1
							|| !Permissions.BALANCE_OTHERS.isAuthorized(user)
							? user
							: getPlayer(args, 0, true)).getMoney();
		user.sendMessage(_("balance", Util.formatCurrency(bal, getContext())));
	}
}
