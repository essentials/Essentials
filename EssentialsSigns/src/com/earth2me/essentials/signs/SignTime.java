package com.earth2me.essentials.signs;

import com.earth2me.essentials.components.economy.ChargeException;
import static com.earth2me.essentials.components.i18n.I18nComponent.$;
import com.earth2me.essentials.Trade;
import com.earth2me.essentials.api.IContext;
import com.earth2me.essentials.components.users.IUserComponent;


public class SignTime extends EssentialsSign
{
	public SignTime()
	{
		super("Time");
	}

	@Override
	protected boolean onSignCreate(final ISign sign, final IUserComponent player, final String username, final IContext ess) throws SignException
	{
		validateTrade(sign, 2, ess);
		final String timeString = sign.getLine(1);
		if ("Day".equalsIgnoreCase(timeString))
		{
			sign.setLine(1, "§2Day");
			return true;
		}
		if ("Night".equalsIgnoreCase(timeString))
		{
			sign.setLine(1, "§2Night");
			return true;
		}
		throw new SignException($("onlyDayNight"));
	}

	@Override
	protected boolean onSignInteract(final ISign sign, final IUserComponent player, final String username, final IContext ess) throws SignException, ChargeException
	{
		final Trade charge = getTrade(sign, 2, ess);
		charge.isAffordableFor(player);
		final String timeString = sign.getLine(1);
		long time = player.getWorld().getTime();
		time -= time % 24000;
		if ("§2Day".equalsIgnoreCase(timeString))
		{
			player.getWorld().setTime(time + 24000);
			charge.charge(player);
			return true;
		}
		if ("§2Night".equalsIgnoreCase(timeString))
		{
			player.getWorld().setTime(time + 37700);
			charge.charge(player);
			return true;
		}
		throw new SignException($("onlyDayNight"));
	}
}
