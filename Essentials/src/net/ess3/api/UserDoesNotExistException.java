package net.ess3.api;

import static net.ess3.I18n._;


public class UserDoesNotExistException extends Exception
{
	/**
	 * Allow serialization of the UserDefinedFileAttributeView exception
	 */
	private static final long serialVersionUID = -6540804196206916310L;

	/**
	 *
	 * @param name
	 */
	public UserDoesNotExistException(String name)
	{
		super(_("§4The user§c {0} §4does not exist.", name));
	}
}
