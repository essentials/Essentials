package net.ess3.signs.signs;

import static net.ess3.I18n._;
import org.bukkit.World;
import net.ess3.api.ChargeException;
import net.ess3.api.IEssentials;
import net.ess3.api.IUser;
import net.ess3.economy.Trade;
import net.ess3.signs.EssentialsSign;


public class SignWeather extends EssentialsSign
{
	public SignWeather()
	{
		super("Weather");
	}

	@Override
	protected boolean onSignCreate(final ISign sign, final IUser player, final String username, final IEssentials ess) throws SignException
	{
		validateTrade(sign, 2, ess);
		final String timeString = sign.getLine(1);
		if ("Sun".equalsIgnoreCase(timeString))
		{
			sign.setLine(1, "§2Sun");
			return true;
		}
		if ("Storm".equalsIgnoreCase(timeString))
		{
			sign.setLine(1, "§2Storm");
			return true;
		}
		throw new SignException(_("onlySunStorm"));
	}

	@Override
	protected boolean onSignInteract(final ISign sign, final IUser player, final String username, final IEssentials ess) throws SignException, ChargeException
	{
		final Trade charge = getTrade(sign, 2, ess);
		charge.isAffordableFor(player);
		final String weatherString = sign.getLine(1);
		final World world = player.getPlayer().getWorld();
		if ("§2Sun".equalsIgnoreCase(weatherString))
		{
			world.setStorm(false);
			charge.charge(player);
			return true;
		}
		if ("§2Storm".equalsIgnoreCase(weatherString))
		{
			world.setStorm(true);
			charge.charge(player);
			return true;
		}
		throw new SignException(_("onlySunStorm"));
	}
}
