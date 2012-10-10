package net.ess3.signs.signs;

import java.util.Locale;
import net.ess3.api.ChargeException;
import net.ess3.api.IEssentials;
import net.ess3.api.IUser;
import net.ess3.economy.Trade;
import net.ess3.permissions.Permissions;
import net.ess3.settings.Kit;
import net.ess3.signs.EssentialsSign;


public class SignKit extends EssentialsSign
{
	public SignKit()
	{
		super("Kit");
	}

	@Override
	protected boolean onSignCreate(final ISign sign, final IUser player, final String username, final IEssentials ess) throws SignException
	{
		validateTrade(sign, 3, ess);

		final String kitName = sign.getLine(1).toLowerCase(Locale.ENGLISH);

		if (kitName.isEmpty())
		{
			sign.setLine(1, "§dKit name!");
			return false;
		}
		else
		{
			try
			{
				ess.getKits().getKit(kitName);
			}
			catch (Exception ex)
			{
				throw new SignException(ex.getMessage(), ex);
			}
			final String group = sign.getLine(2);
			if ("Everyone".equalsIgnoreCase(group) || "Everybody".equalsIgnoreCase(group))
			{
				sign.setLine(2, "§2Everyone");
			}
			return true;
		}
	}

	@Override
	protected boolean onSignInteract(final ISign sign, final IUser player, final String username, final IEssentials ess) throws SignException, ChargeException
	{
		final String kitName = sign.getLine(1).toLowerCase(Locale.ENGLISH);
		final String group = sign.getLine(2);
		if ((!group.isEmpty() && ("§2Everyone".equals(group) || ess.getRanks().inGroup(player, group)))
			|| (group.isEmpty() && Permissions.KITS.isAuthorized(player, kitName)))
		{
			final Trade charge = getTrade(sign, 3, ess);
			charge.isAffordableFor(player);
			try
			{;
				final Kit kit = ess.getKits().getKit(kitName);
				ess.getKits().checkTime(player, kit);
				ess.getKits().sendKit(player, kit);
				charge.charge(player);
			}
			catch (Exception ex)
			{
				throw new SignException(ex.getMessage(), ex);
			}
			return true;
		}
		return false;
	}
}
