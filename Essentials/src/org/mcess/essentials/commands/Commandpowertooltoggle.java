package org.mcess.essentials.commands;

import static org.mcess.essentials.I18n.tl;
import org.mcess.essentials.User;
import org.bukkit.Server;


public class Commandpowertooltoggle extends EssentialsCommand
{
	public Commandpowertooltoggle()
	{
		super("powertooltoggle");
	}

	@Override
	protected void run(final Server server, final User user, final String commandLabel, final String[] args) throws Exception
	{
		if (!user.hasPowerTools())
		{
			user.sendMessage(tl("noPowerTools"));
			return;
		}
		user.sendMessage(user.togglePowerToolsEnabled()
						 ? tl("powerToolsEnabled")
						 : tl("powerToolsDisabled"));
	}
}
