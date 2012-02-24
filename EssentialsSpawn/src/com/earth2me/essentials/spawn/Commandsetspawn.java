package com.earth2me.essentials.spawn;

import static com.earth2me.essentials.I18nComponent._;
import com.earth2me.essentials.components.users.IUser;
import com.earth2me.essentials.components.commands.EssentialsCommand;


public class Commandsetspawn extends EssentialsCommand
{
	@Override
	public void run(final IUser user, final String commandLabel, final String[] args) throws Exception
	{
		final String group = args.length > 0 ? getFinalArg(args, 0) : "default";
		((SpawnStorage)getModule()).setSpawn(user.getLocation(), group);
		user.sendMessage(_("spawnSet", group));
	}
}
