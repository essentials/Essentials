package com.earth2me.essentials.commands;

import static com.earth2me.essentials.I18n._;
import com.earth2me.essentials.User;
import org.bukkit.Server;


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
