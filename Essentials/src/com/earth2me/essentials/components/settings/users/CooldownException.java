package com.earth2me.essentials.components.settings.users;


public class CooldownException extends Exception
{

	public CooldownException(String timeLeft)
	{
		super(timeLeft);
	}

}
