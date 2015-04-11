package org.mcess.essentials.commands;

import org.mcess.essentials.I18n;

public class WarpNotFoundException extends Exception
{
	public WarpNotFoundException()
	{
		super(I18n.tl("warpNotExist"));
	}
	
	public WarpNotFoundException(String message)
	{
		super(message);
	}
}
