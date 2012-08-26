package net.ess3.commands;

import static net.ess3.I18n._;
import net.ess3.api.IUser;
import net.ess3.api.server.Player;
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

		if (args[0].trim().length() < 3)
		{
			throw new NotEnoughArgumentsException("You need to specify a player to pay.");
		}

		double amount = Double.parseDouble(args[1].replaceAll("[^0-9\\.]", ""));

		boolean foundUser = false;
		for (Player p : ess.getUserMap().matchUsers(args[0], false, true))
		{
			user.payUser(p.getUser(), amount);
			Trade.log("Command", "Pay", "Player", user.getName(), new Trade(amount, ess), p.getName(), new Trade(amount, ess), user.getLocation(), ess);
			foundUser = true;
		}

		if (!foundUser)
		{
			throw new NoSuchFieldException(_("playerNotFound"));
		}
	}
}
