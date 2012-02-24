package com.earth2me.essentials.components.commands.handlers;

import com.earth2me.essentials.components.commands.EssentialsCommand;
import static com.earth2me.essentials.I18n._;
import com.earth2me.essentials.components.users.IUser;


public class Commandpowertooltoggle extends EssentialsCommand
{
	@Override
	protected void run(final IUser user, final String commandLabel, final String[] args) throws Exception
	{
		if (!user.getData().hasPowerTools())
		{
			user.sendMessage(_("noPowerTools"));
			return;
		}
		user.acquireWriteLock();
		user.getData().setPowerToolsEnabled(!user.getData().isPowerToolsEnabled());
		user.sendMessage(user.getData().isPowerToolsEnabled()
						 ? _("powerToolsEnabled")
						 : _("powerToolsDisabled"));
	}
}
