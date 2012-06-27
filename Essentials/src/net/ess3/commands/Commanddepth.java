package net.ess3.commands;

import static net.ess3.I18n._;
import net.ess3.api.IUser;


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
