package net.ess3.commands;


public class WarpNotFoundException extends Exception
{
	public WarpNotFoundException()
	{
		super("");
	}
	
	public WarpNotFoundException(String message)
	{
		super(message);
	}
}


