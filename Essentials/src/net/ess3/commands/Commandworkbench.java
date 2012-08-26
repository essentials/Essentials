package net.ess3.commands;

import net.ess3.api.IUser;


public class Commandworkbench extends EssentialsCommand
{
	@Override
	public void run(final IUser user, final String commandLabel, final String[] args) throws Exception
	{
		user.getPlayer().openWorkbench(null, true);
	}
}
