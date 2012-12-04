package com.earth2me.essentials.commands;


public class NoChargeException extends Exception
{
	public NoChargeException()
	{
		super(_("chargeError"));
	}
}
