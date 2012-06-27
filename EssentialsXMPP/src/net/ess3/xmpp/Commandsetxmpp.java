package net.ess3.xmpp;

import net.ess3.api.IUser;
import net.ess3.commands.EssentialsCommand;
import net.ess3.commands.NotEnoughArgumentsException;


public class Commandsetxmpp extends EssentialsCommand
{
	@Override
	protected void run(final IUser user, final String commandLabel, final String[] args) throws NotEnoughArgumentsException
	{
		if (args.length < 1)
		{
			throw new NotEnoughArgumentsException();
		}

		EssentialsXMPP.getInstance().setAddress(user, args[0]);
		user.sendMessage("XMPP address set to " + args[0]);
	}
}
