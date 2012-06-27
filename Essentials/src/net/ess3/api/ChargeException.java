package net.ess3.api;


public class ChargeException extends Exception
{
	public ChargeException(final String message)
	{
		super(message);
	}

	public ChargeException(final String message, final Throwable throwable)
	{
		super(message, throwable);
	}
}
