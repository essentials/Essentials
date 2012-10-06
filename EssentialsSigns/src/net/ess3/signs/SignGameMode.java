package net.ess3.signs;

import java.util.Locale;
import static net.ess3.I18n._;
import net.ess3.api.ChargeException;
import net.ess3.api.IEssentials;
import net.ess3.api.IUser;
import net.ess3.economy.Trade;
import org.bukkit.GameMode;


public class SignGameMode extends EssentialsSign
{
	public SignGameMode()
	{
		super("GameMode");
	}

	@Override
	protected boolean onSignCreate(final ISign sign, final IUser player, final String username, final IEssentials ess) throws SignException
	{
		final String gamemode = sign.getLine(1);
		if (gamemode.isEmpty())
		{
			sign.setLine(1, "Survival");
		}

		validateTrade(sign, 2, ess);

		return true;
	}

	@Override
	protected boolean onSignInteract(final ISign sign, final IUser player, final String username, final IEssentials ess) throws SignException, ChargeException
	{
		final Trade charge = getTrade(sign, 2, ess);
		final String mode = sign.getLine(1).trim();
				
		if (mode.isEmpty())
		{
			throw new SignException(_("invalidSignLine", 2));
		}
		charge.isAffordableFor(player);
		
		//this needs to be fixed
		player.getPlayer().setGameMode(player.getPlayer().getGameMode() == GameMode.SURVIVAL ? GameMode.CREATIVE : GameMode.SURVIVAL);
		player.sendMessage(_("gameMode", _(player.getPlayer().getGameMode().toString().toLowerCase(Locale.ENGLISH)), player.getPlayer().getDisplayName()));
		charge.charge(player);
		return true;
	}
}
