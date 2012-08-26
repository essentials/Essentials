package net.ess3.user;

import static net.ess3.I18n._;

public class PlayerNotFoundException extends Exception {

	public PlayerNotFoundException()
	{
		super(_("playerNotFound"));
	}
}
