package net.ess3.api;


public class InvalidNameException extends Exception
{
	/**
     * Allow serialization of the InvalidNameException exception
     */
    private static final long serialVersionUID = 1485321420293663139L;

	public InvalidNameException(Throwable thrwbl)
	{
		super(thrwbl);
	}
}
