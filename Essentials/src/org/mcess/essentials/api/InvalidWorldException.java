package org.mcess.essentials.api;

import static org.mcess.essentials.I18n.tl;


public class InvalidWorldException extends Exception
{
	private final String world;

	public InvalidWorldException(final String world)
	{
		super(tl("invalidWorld"));
		this.world = world;
	}

	public String getWorld()
	{
		return this.world;
	}
}
