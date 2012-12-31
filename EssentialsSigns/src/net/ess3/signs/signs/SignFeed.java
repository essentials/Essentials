package net.ess3.signs.signs;

import static net.ess3.I18n._;
import net.ess3.api.ChargeException;
import net.ess3.api.IEssentials;
import net.ess3.api.IUser;
import net.ess3.economy.Trade;
import net.ess3.signs.EssentialsSign;


public class SignFeed extends EssentialsSign
{
	public SignFeed()
	{
		super("Feed");
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
		player.getPlayer().setFoodLevel(20);
		player.sendMessage(_("feed"));
		charge.charge(player);
		return true;
	}
}
