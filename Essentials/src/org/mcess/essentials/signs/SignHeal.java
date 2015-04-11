package org.mcess.essentials.signs;

import org.mcess.essentials.ChargeException;
import org.mcess.essentials.Trade;
import org.mcess.essentials.User;
import net.ess3.api.IEssentials;
import org.mcess.essentials.I18n;


public class SignHeal extends EssentialsSign
{
	public SignHeal()
	{
		super("Heal");
	}

	@Override
	protected boolean onSignCreate(final ISign sign, final User player, final String username, final IEssentials ess) throws SignException
	{
		validateTrade(sign, 1, ess);
		return true;
	}

	@Override
	protected boolean onSignInteract(final ISign sign, final User player, final String username, final IEssentials ess) throws SignException, ChargeException
	{
		if (player.getBase().getHealth() == 0)
		{
			throw new SignException(I18n.tl("healDead"));
		}
		final Trade charge = getTrade(sign, 1, ess);
		charge.isAffordableFor(player);
		player.getBase().setHealth(20);
		player.getBase().setFoodLevel(20);
		player.getBase().setFireTicks(0);
		player.sendMessage(I18n.tl("youAreHealed"));
		charge.charge(player);
		Trade.log("Sign", "Heal", "Interact", username, null, username, charge, sign.getBlock().getLocation(), ess);
		return true;
	}
}
