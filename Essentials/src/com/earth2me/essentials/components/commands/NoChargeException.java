package com.earth2me.essentials.components.commands;


public class NoChargeException extends Exception
{
	public NoChargeException()
	{
		super("Will charge later");
	}
}
