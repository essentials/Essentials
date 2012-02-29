package com.earth2me.essentials.components.commands.handlers;

import com.earth2me.essentials.components.users.IUserComponent;
import com.earth2me.essentials.components.commands.EssentialsCommand;
import static com.earth2me.essentials.components.i18n.I18nComponent.$;


public class Commandtptoggle extends EssentialsCommand
{
	@Override
	public void run(final IUserComponent user, final String commandLabel, final String[] args) throws Exception
	{
		user.sendMessage(user.toggleTeleportEnabled()
						 ? $("teleportationEnabled")
						 : $("teleportationDisabled"));
	}
}
