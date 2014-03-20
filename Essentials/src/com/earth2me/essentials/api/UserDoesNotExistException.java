package com.earth2me.essentials.api;

import static com.earth2me.essentials.I18n.tl_;


public class UserDoesNotExistException extends Exception
{
	public UserDoesNotExistException(String name)
	{
		super(tl_("userDoesNotExist", name));
	}
}
