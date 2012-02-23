package com.earth2me.essentials.signs;

import com.earth2me.essentials.api.ChargeException;
import com.earth2me.essentials.Trade;
import com.earth2me.essentials.api.IEssentials;
import com.earth2me.essentials.api.IUser;


public class SignBuy extends EssentialsSign
{
	public SignBuy()
	{
		super("Buy");
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
		final Trade items = getTrade(sign, 1, 2, player, ess);
		final Trade charge = getTrade(sign, 3, ess);
		charge.isAffordableFor(player);
		if (!items.pay(player, false))
		{
			throw new ChargeException("Inventory full");
		}
		charge.charge(player);
		Trade.log("Sign", "Buy", "Interact", username, charge, username, items, sign.getBlock().getLocation(), ess);
		return true;
	}
}
