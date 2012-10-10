package net.ess3.signs.signs;


public class SignException extends Exception
{
	public SignException(final String message)
	{
		super(message);
	}

	public SignException(final String message, final Throwable throwable)
	{
		super(message, throwable);
	}
}
