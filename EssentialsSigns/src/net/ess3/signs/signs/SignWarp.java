package net.ess3.signs.signs;

import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import net.ess3.api.ChargeException;
import net.ess3.api.IEssentials;
import net.ess3.api.IUser;
import net.ess3.economy.Trade;
import net.ess3.permissions.Permissions;
import net.ess3.signs.EssentialsSign;


public class SignWarp extends EssentialsSign
{
	public SignWarp()
	{
		super("Warp");
	}

	@Override
	protected boolean onSignCreate(final ISign sign, final IUser player, final String username, final IEssentials ess) throws SignException
	{
		validateTrade(sign, 3, ess);
		final String warpName = sign.getLine(1);

		if (warpName.isEmpty())
		{
			sign.setLine(1, "§dWarp name!");
			return false;
		}
		else
		{
			try
			{
				ess.getWarps().getWarp(warpName);
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
		final String warpName = sign.getLine(1);
		final String group = sign.getLine(2);

		if ((!group.isEmpty() && ("§2Everyone".equals(group) || ess.getRanks().inGroup(player, group))) || (group.isEmpty() && Permissions.WARPS.isAuthorized(
				player, warpName)))
		{
			final Trade charge = getTrade(sign, 3, ess);
			try
			{
				player.getTeleport().warp(warpName, charge, TeleportCause.PLUGIN);
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
