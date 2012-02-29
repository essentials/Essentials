package com.earth2me.essentials.xmpp;

import com.earth2me.essentials.components.users.IUserComponent;
import com.earth2me.essentials.components.commands.EssentialsCommand;
import com.earth2me.essentials.components.commands.NotEnoughArgumentsException;


public class Commandsetxmpp extends EssentialsCommand
{
	@Override
	protected void run(final IUserComponent user, final String commandLabel, final String[] args) throws NotEnoughArgumentsException
	{
		if (args.length < 1)
		{
			throw new NotEnoughArgumentsException();
		}

		EssentialsXmpp.getInstance().setAddress(user, args[0]);
		user.sendMessage("XMPP address set to " + args[0]);
	}
}
