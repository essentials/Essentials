package net.ess3.user;


public class CooldownException extends Exception
{

	public CooldownException(String timeLeft)
	{
		super(timeLeft);
	}
	
}
