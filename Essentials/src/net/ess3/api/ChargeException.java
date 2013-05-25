package net.ess3.api;


public class ChargeException extends Exception
{
	/**
	 * Allow for serialization of the ChargeException class
	 */
	private static final long serialVersionUID = 200058474023860487L;

	/**
	 *
	 * @param message
	 */
	public ChargeException(final String message)
	{
		super(message);
	}

	/**
	 *
	 * @param message
	 * @param throwable
	 */
	public ChargeException(final String message, final Throwable throwable)
	{
		super(message, throwable);
	}
}
