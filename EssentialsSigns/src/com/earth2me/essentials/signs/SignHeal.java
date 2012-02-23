package com.earth2me.essentials.signs;

import com.earth2me.essentials.api.ChargeException;
import static com.earth2me.essentials.I18n._;
import com.earth2me.essentials.Trade;
import com.earth2me.essentials.api.IEssentials;
import com.earth2me.essentials.api.IUser;


public class SignHeal extends EssentialsSign
{
	public SignHeal()
	{
		super("Heal");
	}

	@Override
	protected boolean onSignCreate(final ISign sign, final IUser player, final String username, final IEssentials ess) throws SignException
	{
		validateTrade(sign, 1, ess);
		return true;
	}

	@Override
	protected boolean onSignInteract(final ISign sign, final IUser player, final String username, final IEssentials ess) throws SignException, ChargeException
	{
		final Trade charge = getTrade(sign, 1, ess);
		charge.isAffordableFor(player);
		player.setHealth(20);
		player.setFoodLevel(20);
		player.setFireTicks(0);
		player.sendMessage(_("youAreHealed"));
		charge.charge(player);
		return true;
	}
}
