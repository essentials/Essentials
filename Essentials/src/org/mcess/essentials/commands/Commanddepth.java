package org.mcess.essentials.commands;

import org.mcess.essentials.User;
import org.bukkit.Server;
import org.mcess.essentials.I18n;


public class Commanddepth extends EssentialsCommand
{
	public Commanddepth()
	{
		super("depth");
	}

	@Override
	public void run(final Server server, final User user, final String commandLabel, final String[] args) throws Exception
	{
		final int depth = user.getLocation().getBlockY() - 63;
		if (depth > 0)
		{
			user.sendMessage(I18n.tl("depthAboveSea", depth));
		}
		else if (depth < 0)
		{
			user.sendMessage(I18n.tl("depthBelowSea", (-depth)));
		}
		else
		{
			user.sendMessage(I18n.tl("depth"));
		}
	}
}
