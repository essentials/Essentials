package com.earth2me.essentials.commands;

import static com.earth2me.essentials.I18n._;
import com.earth2me.essentials.User;
import com.earth2me.essentials.utils.NumberUtil;
import java.math.BigDecimal;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;


public class Commandbalance extends EssentialsCommand
{
	public Commandbalance()
	{
		super("balance");
	}

	@Override
	protected void run(final Server server, final CommandSender sender, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 1)
		{
			throw new NotEnoughArgumentsException();
		}

		User target = getPlayer(server, args, 0, true, true);
		sender.sendMessage(_("balanceOther", target.getDisplayName(), NumberUtil.displayCurrency(target.getMoney(), ess)));
	}

	@Override
	public void run(final Server server, final User user, final String commandLabel, final String[] args) throws Exception
	{

		if (args.length < 1 || !user.isAuthorized("essentials.balance.others"))
		{
			final BigDecimal bal = user.getMoney();
			user.sendMessage(_("balance", NumberUtil.displayCurrency(bal, ess)));
		}
		else
		{
			final User target = getPlayer(server, args, 0, true, true);
			final BigDecimal bal = target.getMoney();
			user.sendMessage(_("balanceOther", target.getDisplayName(), NumberUtil.displayCurrency(bal, ess)));
		}
	}
}
