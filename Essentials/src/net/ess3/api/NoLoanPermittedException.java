package net.ess3.api;

import static net.ess3.I18n._;


public class NoLoanPermittedException extends Exception
{
	/**
	 * Allow serialization of the NoLoanPermittedException exception
	 */
	private static final long serialVersionUID = 1897047051293914036L;

	public NoLoanPermittedException()
	{
		super(_("negativeBalanceError"));
	}
}
