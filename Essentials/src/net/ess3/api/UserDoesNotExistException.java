package net.ess3.api;

import static org.mcess.essentials.I18n.tl;


public class UserDoesNotExistException extends Exception
{
	public UserDoesNotExistException(String name)
	{
		super(tl("userDoesNotExist", name));
	}
}
