package net.ess3.commands;


public class NotEnoughArgumentsException extends Exception
{
	private static final long serialVersionUID = 4659884540230373059L;

	public NotEnoughArgumentsException()
	{
		super("");
	}

	public NotEnoughArgumentsException(final String string)
	{
		super(string);
	}

	public NotEnoughArgumentsException(final Throwable ex)
	{
		super("", ex);
	}
}
