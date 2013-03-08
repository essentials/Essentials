package com.earth2me.essentials.signs;

import com.earth2me.essentials.ISettings;
import com.earth2me.essentials.ChargeException;
import com.earth2me.essentials.IEssentials;
import com.earth2me.essentials.Trade;
import com.earth2me.essentials.User;
import org.bukkit.inventory.ItemStack;


public class SignBuy extends EssentialsSign
{
	public SignBuy()
	{
		super("Buy");
	}

	@Override
	protected boolean onSignCreate(final ISign sign, final User player, final String username, final IEssentials ess) throws SignException
	{
		ISettings settings = ess.getSettings() ;
		final double scale = settings.getBuyScale() ;
		
		final ItemStack item = getItemStack(sign.getLine(2), 1, ess);
		validateTrade(sign, 1, 2, player, ess);
		validateTrade(sign, 3, 1, item, scale, ess) ;
		
		return true ;
	}

	@Override
	protected boolean onSignInteract(final ISign sign, final User player, final String username, final IEssentials ess) throws SignException, ChargeException
	{
		ISettings settings = ess.getSettings() ;
		final double scale = settings.getBuyScale() ;
		final ItemStack item = getItemStack(sign.getLine(2), 1, ess);
		
		final Trade items = getTrade(sign, 1, 2, player, ess);
		final Trade charge = getTrade(sign, 3, 1, item, scale, ess);
		charge.isAffordableFor(player);
		if (!items.pay(player, false))
		{
			throw new ChargeException("Inventory full"); //TODO: TL
		}
		charge.charge(player);
		Trade.log("Sign", "Buy", "Interact", username, charge, username, items, sign.getBlock().getLocation(), ess);
		return true;
	}
}
