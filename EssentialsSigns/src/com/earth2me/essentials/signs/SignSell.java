package com.earth2me.essentials.signs;

import net.ess3.api.ChargeException;
import net.ess3.economy.Trade;
import net.ess3.api.IEssentials;
import net.ess3.api.IUser;


public class SignSell extends EssentialsSign
{
	public SignSell()
	{
		super("Sell");
	}

	@Override
	protected boolean onSignCreate(final ISign sign, final IUser player, final String username, final IEssentials ess) throws SignException
	{
		validateTrade(sign, 1, 2, player, ess);
		validateTrade(sign, 3, ess);
		return true;
	}

	@Override
	protected boolean onSignInteract(final ISign sign, final IUser player, final String username, final IEssentials ess) throws SignException, ChargeException
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
