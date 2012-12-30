package net.ess3.user;


public class CooldownException extends Exception
{

    private static final long serialVersionUID = 913632836257457319L;

	public CooldownException(String timeLeft)
	{
		super(timeLeft);
	}
	
}
