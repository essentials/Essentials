package net.ess3.user;

import static net.ess3.I18n._;

public class PlayerNotFoundException extends Exception {

    private static final long serialVersionUID = -510752839980332640L;

	public PlayerNotFoundException()
	{
		super(_("playerNotFound"));
	}
}
