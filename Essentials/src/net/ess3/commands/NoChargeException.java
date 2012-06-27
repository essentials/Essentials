package net.ess3.commands;


public class NoChargeException extends Exception
{
	public NoChargeException()
	{
		super("Will charge later");
	}
}
