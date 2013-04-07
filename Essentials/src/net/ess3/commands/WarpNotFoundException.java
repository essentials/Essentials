package net.ess3.commands;

import static net.ess3.I18n._;


public class WarpNotFoundException extends Exception
{
	private static final long serialVersionUID = 6585692783437952812L;

	public WarpNotFoundException()
	{
		super(_("§4That warp does not exist."));
	}

	public WarpNotFoundException(String message)
	{
		super(message);
	}
}
