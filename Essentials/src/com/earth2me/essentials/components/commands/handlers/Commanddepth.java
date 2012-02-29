package com.earth2me.essentials.components.commands.handlers;

import com.earth2me.essentials.components.users.IUserComponent;
import com.earth2me.essentials.components.commands.EssentialsCommand;
import static com.earth2me.essentials.components.i18n.I18nComponent.$;


public class Commanddepth extends EssentialsCommand
{
	@Override
	public void run(final IUserComponent user, final String commandLabel, final String[] args) throws Exception
	{
		final int depth = user.getLocation().getBlockY() - 63;
		if (depth > 0)
		{
			user.sendMessage($("depthAboveSea", depth));
		}
		else if (depth < 0)
		{
			user.sendMessage($("depthBelowSea", (-depth)));
		}
		else
		{
			user.sendMessage($("depth"));
		}
	}
}
