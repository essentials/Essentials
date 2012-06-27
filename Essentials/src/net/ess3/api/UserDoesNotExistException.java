package net.ess3.api;

import static net.ess3.I18n._;


public class UserDoesNotExistException extends Exception
{
	public UserDoesNotExistException(String name)
	{
		super(_("userDoesNotExist", name));
	}
}
