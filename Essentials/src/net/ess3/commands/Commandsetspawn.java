package net.ess3.commands;

import static net.ess3.I18n._;
import net.ess3.api.IUser;


public class Commandsetspawn extends EssentialsCommand
{
	@Override
	public void run(final IUser user, final String commandLabel, final String[] args) throws Exception
	{
		final String group = args.length > 0 ? getFinalArg(args, 0) : "default";
		ess.getSpawns().setSpawn(user.getPlayer().getLocation(), group);
		user.sendMessage(_("spawnSet", group));
	}
}
