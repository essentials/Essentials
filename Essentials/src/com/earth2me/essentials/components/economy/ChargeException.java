package com.earth2me.essentials.components.economy;


public class ChargeException extends Exception
{
	private static final long serialVersionUID = 928374982734L;

	public ChargeException(final String message)
	{
		super(message);
	}

	public ChargeException(final String message, final Throwable throwable)
	{
		super(message, throwable);
	}
}
