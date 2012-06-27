package net.ess3.api;

import static net.ess3.I18n._;


public class NoLoanPermittedException extends Exception
{
	public NoLoanPermittedException()
	{
		super(_("negativeBalanceError"));
	}
}
