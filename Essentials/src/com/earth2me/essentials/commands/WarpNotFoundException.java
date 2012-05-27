package com.earth2me.essentials.commands;


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


