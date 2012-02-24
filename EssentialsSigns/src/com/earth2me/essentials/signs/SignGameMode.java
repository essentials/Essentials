package com.earth2me.essentials.signs;

import com.earth2me.essentials.api.ChargeException;
import static com.earth2me.essentials.I18n._;
import com.earth2me.essentials.Trade;
import com.earth2me.essentials.api.IContext;
import com.earth2me.essentials.components.users.IUser;
import java.util.Locale;
import org.bukkit.GameMode;


public class SignGameMode extends EssentialsSign
{
	public SignGameMode()
	{
		super("GameMode");
	}

	@Override
	protected boolean onSignCreate(final ISign sign, final IUser player, final String username, final IContext ess) throws SignException
	{
		validateTrade(sign, 1, ess);
		return true;
	}

	@Override
	protected boolean onSignInteract(final ISign sign, final IUser player, final String username, final IContext ess) throws SignException, ChargeException
	{
		final Trade charge = getTrade(sign, 1, ess);
		charge.isAffordableFor(player);

		player.setGameMode(player.getGameMode() == GameMode.SURVIVAL ? GameMode.CREATIVE : GameMode.SURVIVAL);
		player.sendMessage(_("gameMode", _(player.getGameMode().toString().toLowerCase(Locale.ENGLISH)), player.getDisplayName()));
		charge.charge(player);
		return true;
	}
}
