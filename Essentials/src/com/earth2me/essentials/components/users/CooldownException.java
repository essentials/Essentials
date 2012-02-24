package com.earth2me.essentials.components.users;


public class CooldownException extends Exception
{

	public CooldownException(String timeLeft)
	{
		super(timeLeft);
	}
	
}
