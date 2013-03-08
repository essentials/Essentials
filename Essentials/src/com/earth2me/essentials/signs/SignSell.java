package com.earth2me.essentials.signs;

import com.earth2me.essentials.ChargeException;
import com.earth2me.essentials.IEssentials;
import com.earth2me.essentials.Trade;
import com.earth2me.essentials.User;
import org.bukkit.inventory.ItemStack;


public class SignSell extends EssentialsSign
{
	public SignSell()
	{
		super("Sell");
	}

	@Override
	protected boolean onSignCreate(final ISign sign, final User player, final String username, final IEssentials ess) throws SignException
	{
		final double scale = ess.getSettings().getSellScale();
		final ItemStack item = getItemStack(sign.getLine(2), 1, ess);
		validateTrade(sign, 1, 2, player, ess);
		validateTrade(sign, 3, item, scale, ess);
		return true;
	}

	@Override
	protected boolean onSignInteract(final ISign sign, final User player, final String username, final IEssentials ess) throws SignException, ChargeException
	{
		final double scale = ess.getSettings().getSellScale() ;
	
		final ItemStack item = getItemStack(sign.getLine(2), 1, ess);	
		final Trade charge = getTrade(sign, 1, 2, player, ess);
		final Trade money = getTrade(sign, 3, item, scale, ess);
		charge.isAffordableFor(player);
		money.pay(player);
		charge.charge(player);
		Trade.log("Sign", "Sell", "Interact", username, charge, username, money, sign.getBlock().getLocation(), ess);
		return true;
	}
}
