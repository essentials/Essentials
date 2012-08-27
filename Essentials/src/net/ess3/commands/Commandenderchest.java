package net.ess3.commands;

import net.ess3.user.User;
import org.bukkit.Server;


public class Commandenderchest extends EssentialsCommand
{

	@Override
	protected void run(final Server server, final User user, final String commandLabel, final String[] args) throws Exception
	{		
		if (args.length > 0 && user.isAuthorized("essentials.enderchest.others"))
		{
			final User invUser = getPlayer(server, args, 0);
			user.openInventory(invUser.getEnderChest());
			user.setEnderSee(true);
		}
		else
		{
			user.openInventory(user.getEnderChest());
			user.setEnderSee(false);
		}

	}
}
