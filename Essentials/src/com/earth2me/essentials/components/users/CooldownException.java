package com.earth2me.essentials.components.users;


public class CooldownException extends Exception
{

	public CooldownException(final String timeLeft)
	{
		super(timeLeft);
	}

}
