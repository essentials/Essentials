package net.ess3.signs.signs;

import net.ess3.SpawnMob;
import net.ess3.api.ChargeException;
import net.ess3.api.IEssentials;
import net.ess3.api.IUser;
import net.ess3.economy.Trade;
import net.ess3.signs.EssentialsSign;


public class SignSpawnmob extends EssentialsSign
{
	public SignSpawnmob()
	{
		super("Spawnmob");
	}

	@Override
	protected boolean onSignCreate(ISign sign, IUser player, String username, IEssentials ess) throws SignException, ChargeException
	{
		validateInteger(sign, 1);
		validateTrade(sign, 3, ess);
		return true;
	}

	@Override
	protected boolean onSignInteract(ISign sign, IUser player, String username, IEssentials ess) throws SignException, ChargeException
	{
		final Trade charge = getTrade(sign, 3, ess);
		charge.isAffordableFor(player);
		try
		{
			String[] mobData = SpawnMob.mobData(sign.getLine(2));
			SpawnMob.spawnmob(ess, ess.getServer(), player, player, mobData, Integer.parseInt(sign.getLine(1)));
		}
		catch (Exception ex)
		{
			throw new SignException(ex.getMessage(), ex);
		}
		charge.charge(player);
		return true;
	}
}
