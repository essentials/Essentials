package org.mcess.essentials.signs;

import org.mcess.essentials.ChargeException;
import org.mcess.essentials.Trade;
import org.mcess.essentials.User;
import net.ess3.api.IEssentials;
import net.ess3.api.MaxMoneyException;


public class SignBuy extends EssentialsSign
{
	public SignBuy()
	{
		super("Buy");
	}

	@Override
	protected boolean onSignCreate(final ISign sign, final User player, final String username, final IEssentials ess) throws SignException
	{
		validateTrade(sign, 1, 2, player, ess);
		validateTrade(sign, 3, ess);
		return true;
	}

	@Override
	protected boolean onSignInteract(final ISign sign, final User player, final String username, final IEssentials ess) throws SignException, ChargeException, MaxMoneyException
	{
		final Trade items = getTrade(sign, 1, 2, player, ess);
		final Trade charge = getTrade(sign, 3, ess);
		charge.isAffordableFor(player);
		if (!items.pay(player))
		{
			throw new ChargeException("Inventory full"); //TODO: TL
		}
		charge.charge(player);
		Trade.log("Sign", "Buy", "Interact", username, charge, username, items, sign.getBlock().getLocation(), ess);
		return true;
	}
}
