package net.ess3.api;

import static com.earth2me.essentials.I18n.tl_;


public class UserDoesNotExistException extends Exception
{
	public UserDoesNotExistException(String name)
	{
		super(tl_("userDoesNotExist", name));
	}
}
