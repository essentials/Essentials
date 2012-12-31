package net.ess3.commands;


public class NoChargeException extends Exception
{
    private static final long serialVersionUID = 5817092912429182826L;

	public NoChargeException()
	{
		super("Will charge later");
	}
}
