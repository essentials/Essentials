package net.ess3.commands;

import static net.ess3.I18n._;
import net.ess3.api.IUser;


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
		user.getData().setPowerToolsEnabled(!user.getData().isPowerToolsEnabled());
		user.queueSave();
		user.sendMessage(user.getData().isPowerToolsEnabled()
						 ? _("powerToolsEnabled")
						 : _("powerToolsDisabled"));
	}
}
