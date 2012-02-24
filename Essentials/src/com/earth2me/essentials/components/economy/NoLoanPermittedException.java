package com.earth2me.essentials.components.economy;

import static com.earth2me.essentials.I18n._;


public class NoLoanPermittedException extends Exception
{
	public NoLoanPermittedException()
	{
		super(_("negativeBalanceError"));
	}
}
