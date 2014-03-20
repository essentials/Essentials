package com.earth2me.essentials.commands;

import static com.earth2me.essentials.I18n.tl_;

public class WarpNotFoundException extends Exception
{
	public WarpNotFoundException()
	{
		super(tl_("warpNotExist"));
	}
	
	public WarpNotFoundException(String message)
	{
		super(message);
	}
}
