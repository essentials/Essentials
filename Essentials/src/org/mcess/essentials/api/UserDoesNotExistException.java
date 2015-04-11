package org.mcess.essentials.api;

import org.mcess.essentials.I18n;


public class UserDoesNotExistException extends Exception
{
	public UserDoesNotExistException(String name)
	{
		super(I18n.tl("userDoesNotExist", name));
	}
}
