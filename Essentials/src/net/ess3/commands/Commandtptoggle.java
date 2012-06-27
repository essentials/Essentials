package net.ess3.commands;

import static net.ess3.I18n._;
import net.ess3.api.IUser;


public class Commandtptoggle extends EssentialsCommand
{
	@Override
	public void run(final IUser user, final String commandLabel, final String[] args) throws Exception
	{
		user.sendMessage(user.toggleTeleportEnabled()
						 ? _("teleportationEnabled")
						 : _("teleportationDisabled"));
	}
}
