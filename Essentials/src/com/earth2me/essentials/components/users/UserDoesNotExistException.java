package com.earth2me.essentials.components.users;

import static com.earth2me.essentials.components.i18n.I18nComponent.$;


public class UserDoesNotExistException extends Exception
{
	public UserDoesNotExistException(String name)
	{
		super($("userDoesNotExist", name));
	}
}
