package net.ess3.commands;

import static net.ess3.I18n._;
import net.ess3.api.IUser;
import net.ess3.economy.Trade;



public class Commandpay extends EssentialsCommand
{
	@Override
	public void run(final IUser user, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 2)
		{
			throw new NotEnoughArgumentsException();
		}

		double amount = Double.parseDouble(args[1].replaceAll("[^0-9\\.]", ""));
		
		if (amount <= 0 || Double.isNaN(amount) || Double.isInfinite(amount)) {
			user.sendMessage(_("invalidAmount"));
		}

		boolean foundUser = false;
		for (IUser u : ess.getUserMap().matchUsers(args[0], true))
		{
			user.payUser(u, amount);
			Trade.log("Command", "Pay", "Player", user.getName(), new Trade(amount, ess), u.getName(), new Trade(amount, ess), user.getPlayer().getLocation(), ess);
			foundUser = true;
		}

		if (!foundUser)
		{
			throw new NoSuchFieldException(_("playerNotFound"));
		}
	}
}
