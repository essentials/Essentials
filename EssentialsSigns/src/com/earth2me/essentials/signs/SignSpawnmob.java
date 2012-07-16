package com.earth2me.essentials.signs;

import net.ess3.api.ChargeException;
import net.ess3.economy.Trade;
import net.ess3.api.IEssentials;
import net.ess3.api.IUser;
import net.ess3.commands.Commandspawnmob;


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

	
	//TODO: This should call a method not a command
	@Override
	protected boolean onSignInteract(ISign sign, IUser player, String username, IEssentials ess) throws SignException, ChargeException
	{
		final Trade charge = getTrade(sign, 3, ess);
		charge.isAffordableFor(player);
		Commandspawnmob command = new Commandspawnmob();	
		command.init(ess, "spawnmob");
		String[] args = new String[]
		{
			sign.getLine(2), sign.getLine(1)
		};
		try
		{
			command.run(player, "spawnmob", args);
		}
		catch (Exception ex)
		{
			throw new SignException(ex.getMessage(), ex);
		}
		charge.charge(player);
		return true;
	}
}
