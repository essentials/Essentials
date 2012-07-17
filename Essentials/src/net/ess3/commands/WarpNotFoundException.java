package net.ess3.commands;
import static net.ess3.I18n._;


public class WarpNotFoundException extends Exception
{
	public WarpNotFoundException()
	{
		super(_("warpNotExist"));
	}
	
	public WarpNotFoundException(String message)
	{
		super(message);
	}
}


