package com.earth2me.essentials.components.commands.handlers;

import com.earth2me.essentials.Trade;
import com.earth2me.essentials.components.commands.EssentialsCommand;
import com.earth2me.essentials.components.commands.NotEnoughArgumentsException;
import static com.earth2me.essentials.components.i18n.I18nComponent._;
import com.earth2me.essentials.components.users.IUserComponent;
import org.bukkit.entity.Player;


public class Commandpay extends EssentialsCommand
{
	@Override
	public void run(final IUserComponent user, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 2)
		{
			throw new NotEnoughArgumentsException();
		}

		if (args[0] == "")
		{
			throw new NotEnoughArgumentsException("You need to specify a player to pay.");
		}

		double amount = Double.parseDouble(args[1].replaceAll("[^0-9\\.]", ""));

		Boolean foundUser = false;
		for (Player p : getServer().matchPlayer(args[0]))
		{
			IUserComponent u = getContext().getUser(p);
			if (u.isHidden())
			{
				continue;
			}
			user.payUser(u, amount);
			Trade.log("Command", "Pay", "Player", user.getName(), new Trade(amount, getContext()), u.getName(), new Trade(amount, getContext()), user.getLocation(), getContext());
			foundUser = true;
		}

		if (foundUser == false)
		{
			throw new NoSuchFieldException(_("playerNotFound"));
		}
	}
}
