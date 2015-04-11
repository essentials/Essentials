package org.mcess.essentials.commands;

import org.mcess.essentials.User;
import org.bukkit.Server;


public class Commandworkbench extends EssentialsCommand
{
	public Commandworkbench()
	{
		super("workbench");
	}


	@Override
	public void run(final Server server, final User user, final String commandLabel, final String[] args) throws Exception
	{
		user.getBase().openWorkbench(null, true);
	}
}