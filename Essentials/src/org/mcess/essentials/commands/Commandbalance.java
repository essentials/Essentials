package org.mcess.essentials.commands;

import org.mcess.essentials.CommandSource;
import org.mcess.essentials.User;
import org.mcess.essentials.utils.NumberUtil;
import java.math.BigDecimal;
import org.bukkit.Server;
import org.mcess.essentials.I18n;


public class Commandbalance extends EssentialsCommand
{
	public Commandbalance()
	{
		super("balance");
	}

	@Override
	protected void run(final Server server, final CommandSource sender, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 1)
		{
			throw new NotEnoughArgumentsException();
		}

		User target = getPlayer(server, args, 0, true, true);
		sender.sendMessage(I18n.tl("balanceOther", target.isHidden() ? target.getName() : target.getDisplayName(), NumberUtil.displayCurrency(target.getMoney(), ess)));
	}

	@Override
	public void run(final Server server, final User user, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length == 1 && user.isAuthorized("essentials.balance.others"))
		{
			final User target = getPlayer(server, args, 0, true, true);
			final BigDecimal bal = target.getMoney();
			user.sendMessage(I18n.tl("balanceOther", target.isHidden() ? target.getName() : target.getDisplayName(), NumberUtil.displayCurrency(bal, ess)));
		}
		else if (args.length < 2)
		{
			final BigDecimal bal = user.getMoney();
			user.sendMessage(I18n.tl("balance", NumberUtil.displayCurrency(bal, ess)));
		}
		else
		{
			throw new NotEnoughArgumentsException();
		}
	}
}
