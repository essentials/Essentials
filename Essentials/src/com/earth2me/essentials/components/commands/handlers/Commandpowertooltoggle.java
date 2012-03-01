package com.earth2me.essentials.components.commands.handlers;

import com.earth2me.essentials.components.commands.EssentialsCommand;
import static com.earth2me.essentials.components.i18n.I18nComponent.$;
import com.earth2me.essentials.components.users.IUserComponent;


public class Commandpowertooltoggle extends EssentialsCommand
{
	@Override
	protected void run(final IUserComponent user, final String commandLabel, final String[] args) throws Exception
	{
		if (!user.getData().hasPowerTools())
		{
			user.sendMessage($("noPowerTools"));
			return;
		}
		user.acquireWriteLock();
		user.getData().setPowerToolsEnabled(!user.getData().isPowerToolsEnabled());
		user.sendMessage(user.getData().isPowerToolsEnabled()
						 ? $("powerToolsEnabled")
						 : $("powerToolsDisabled"));
	}
}
