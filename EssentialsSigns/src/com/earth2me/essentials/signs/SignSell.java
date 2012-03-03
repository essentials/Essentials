package com.earth2me.essentials.signs;

import com.earth2me.essentials.components.economy.ChargeException;
import com.earth2me.essentials.Trade;
import com.earth2me.essentials.api.IContext;
import com.earth2me.essentials.components.users.IUserComponent;


public class SignSell extends EssentialsSign
{
	public SignSell()
	{
		super("Sell");
	}

	@Override
	protected boolean onSignCreate(final ISign sign, final IUserComponent player, final String username, final IContext ess) throws SignException
	{
		validateTrade(sign, 1, 2, player, ess);
		validateTrade(sign, 3, ess);
		return true;
	}

	@Override
	protected boolean onSignInteract(final ISign sign, final IUserComponent player, final String username, final IContext ess) throws SignException, ChargeException
	{
		final Trade charge = getTrade(sign, 1, 2, player, ess);
		final Trade money = getTrade(sign, 3, ess);
		charge.isAffordableFor(player);
		money.pay(player);
		charge.charge(player);
		Trade.log("Sign", "Sell", "Interact", username, charge, username, money, sign.getBlock().getLocation(), ess);
		return true;
	}
}
