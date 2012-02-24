package com.earth2me.essentials.components.commands.handlers;

import com.earth2me.essentials.components.commands.EssentialsCommand;
import static com.earth2me.essentials.I18nComponent._;
import com.earth2me.essentials.components.users.IUser;


public class Commanddepth extends EssentialsCommand
{
	@Override
	public void run(final IUser user, final String commandLabel, final String[] args) throws Exception
	{
		final int depth = user.getLocation().getBlockY() - 63;
		if (depth > 0)
		{
			user.sendMessage(_("depthAboveSea", depth));
		}
		else if (depth < 0)
		{
			user.sendMessage(_("depthBelowSea", (-depth)));
		}
		else
		{
			user.sendMessage(_("depth"));
		}
	}
}
