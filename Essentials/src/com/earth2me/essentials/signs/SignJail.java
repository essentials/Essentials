/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.earth2me.essentials.signs;

import static com.earth2me.essentials.I18n._;
import com.earth2me.essentials.IEssentials;
import com.earth2me.essentials.User;


/**
 *
 * @author Janus
 */
public class SignJail extends EssentialsSign
{
	SignJail() {
		super("Jail");
	}
	
	@Override
	protected boolean onSignCreate(final ISign sign, final User player, final String username, final IEssentials ess) throws SignException
	{
		validateTrade(sign, 2, ess);
		final String timeString = sign.getLine(1);
		if ("Time".equalsIgnoreCase(timeString))
		{
			sign.setLine(1, "§4Time");
			return true;
		}
		sign.setLine(1, "§c<time>");
		throw new SignException(_("invalidSignLine", 1));
	}

	@Override
	protected boolean onSignInteract(final ISign sign, final User player, final String username, final IEssentials ess) throws SignException
	{
		final String modeString = sign.getLine(1);
		if ("§4Time".equalsIgnoreCase(modeString))
		{
			ess.getJails().jailtimeSelf(player);
			return true;
		}
		return true;
	}
	
	@Override
	public boolean getIgnoreJailed()
	{
		return true;
	}
}
