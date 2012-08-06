package net.ess3.commands;

import net.ess3.api.server.Server;
import net.ess3.user.User;


public class Commandworkbench extends EssentialsCommand
{
	public Commandworkbench()
	{
		super("workbench");
	}


	@Override
	public void run(final Server server, final User user, final String commandLabel, final String[] args) throws Exception
	{
		user.openWorkbench(null, true);
	}
}
