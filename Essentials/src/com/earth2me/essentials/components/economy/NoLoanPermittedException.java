package com.earth2me.essentials.components.economy;

import static com.earth2me.essentials.components.i18n.I18nComponent._;


public class NoLoanPermittedException extends Exception
{
	private static final long serialVersionUID = 189659186583L;
	
	public NoLoanPermittedException()
	{
		super(_("negativeBalanceError"));
	}
}
