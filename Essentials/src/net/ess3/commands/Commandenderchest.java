package net.ess3.commands;

import net.ess3.api.IUser;
import net.ess3.permissions.Permissions;


public class Commandenderchest extends EssentialsCommand
{

	@Override
	protected void run(final IUser user, final String commandLabel, final String[] args) throws Exception
	{		
		if (args.length > 0 && Permissions.ENDERCHEST_OTHERS.isAuthorized(user))
		{
			final IUser invUser = ess.getUserMap().getUser(args[0]);
			user.getPlayer().openInventory(invUser.getPlayer().getEnderChest());
			user.setEnderSee(true);
		}
		else
		{
			user.getPlayer().openInventory(user.getPlayer().getEnderChest());
			user.setEnderSee(false);
		}

	}
}
