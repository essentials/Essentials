package net.ess3.api.events;

import org.mcess.essentials.signs.EssentialsSign;
import net.ess3.api.IUser;


public class SignCreateEvent extends SignEvent
{
	public SignCreateEvent(EssentialsSign.ISign sign, EssentialsSign essSign, IUser user)
	{
		super(sign, essSign, user);
	}
}
