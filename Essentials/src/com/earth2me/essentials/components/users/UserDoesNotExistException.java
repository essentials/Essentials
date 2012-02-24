package com.earth2me.essentials.components.users;

import static com.earth2me.essentials.I18nComponent._;


public class UserDoesNotExistException extends Exception
{
	public UserDoesNotExistException(String name)
	{
		super(_("userDoesNotExist", name));
	}
}
