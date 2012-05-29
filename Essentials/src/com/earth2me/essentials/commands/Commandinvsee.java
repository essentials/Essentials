package com.earth2me.essentials.commands;

import com.earth2me.essentials.api.IUser;


public class Commandinvsee extends EssentialsCommand
{
	@Override
	protected void run(final IUser user, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 1)
		{
			throw new NotEnoughArgumentsException();
		}
		final IUser invUser = getPlayer(args, 0);	
		user.setInvSee(true);
		user.openInventory(invUser.getInventory());		
	}
}
