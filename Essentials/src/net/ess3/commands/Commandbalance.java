package net.ess3.commands;

import static net.ess3.I18n._;
import net.ess3.api.IUser;
import net.ess3.permissions.Permissions;
import net.ess3.utils.FormatUtil;
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
		sender.sendMessage(_("§aBalance:§c {0}", FormatUtil.displayCurrency(ess.getUserMap().matchUser(args[0], true).getMoney(), ess)));
	}

	@Override
	public void run(final IUser user, final String commandLabel, final String[] args) throws Exception
	{
		final double bal = (args.length < 1 || !Permissions.BALANCE_OTHERS.isAuthorized(user) ? user : ess.getUserMap().matchUser(args[0], true)).getMoney();
		user.sendMessage(_("§aBalance:§c {0}", FormatUtil.displayCurrency(bal, ess)));
	}
}
