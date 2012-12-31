package net.ess3.api;

import static net.ess3.I18n._;


public class UserDoesNotExistException extends Exception
{
	/**
	 * Allow serialization of the UserDefinedFileAttributeView exception
	 */
	private static final long serialVersionUID = -6540804196206916310L;

	public UserDoesNotExistException(String name)
	{
		super(_("userDoesNotExist", name));
	}
}
