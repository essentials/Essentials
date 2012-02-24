package com.earth2me.essentials.components.commands.handlers;

import com.earth2me.essentials.components.commands.EssentialsCommand;
import static com.earth2me.essentials.I18n._;
import com.earth2me.essentials.components.users.IUser;


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
