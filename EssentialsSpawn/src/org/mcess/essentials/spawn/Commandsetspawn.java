package org.mcess.essentials.spawn;

import org.mcess.essentials.User;
import org.mcess.essentials.commands.EssentialsCommand;
import org.bukkit.Server;
import org.mcess.essentials.I18n;


public class Commandsetspawn extends EssentialsCommand
{
	public Commandsetspawn()
	{
		super("setspawn");
	}

	@Override
	public void run(final Server server, final User user, final String commandLabel, final String[] args) throws Exception
	{
		final String group = args.length > 0 ? getFinalArg(args, 0) : "default";
		((SpawnStorage)module).setSpawn(user.getLocation(), group);
		user.sendMessage(I18n.tl("spawnSet", group));
	}
}
