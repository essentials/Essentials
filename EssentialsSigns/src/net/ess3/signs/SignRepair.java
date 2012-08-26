package net.ess3.signs;

import static net.ess3.I18n._;
import net.ess3.api.ChargeException;
import net.ess3.api.IEssentials;
import net.ess3.api.IUser;
import net.ess3.commands.Commandrepair;
import net.ess3.economy.Trade;


public class SignRepair extends EssentialsSign
{
	public SignRepair()
	{
		super("Repair");
	}

	@Override
	protected boolean onSignCreate(final ISign sign, final IUser player, final String username, final IEssentials ess) throws SignException
	{
		final String repairTarget = sign.getLine(1);
		if (repairTarget.isEmpty())
		{
			sign.setLine(1, "Hand");
		}
		else if (!repairTarget.equalsIgnoreCase("all") && !repairTarget.equalsIgnoreCase("hand") )
		{
			throw new SignException(_("invalidSignLine", 2));
		}		
		validateTrade(sign, 2, ess);
		return true;
	}

	@Override
	protected boolean onSignInteract(final ISign sign, final IUser player, final String username, final IEssentials ess) throws SignException, ChargeException
	{
		final Trade charge = getTrade(sign, 2, ess);
		charge.isAffordableFor(player);
		
		Commandrepair command = new Commandrepair();
		command.init(ess, "repair");
		String[] args = new String[]
		{
			sign.getLine(1)
		};
		try
		{
			command.run(player, "repair", args);
		}
		catch (Exception ex)
		{
			throw new SignException(ex.getMessage(), ex);
		}
		charge.charge(player);					
		return true;
	}
}
