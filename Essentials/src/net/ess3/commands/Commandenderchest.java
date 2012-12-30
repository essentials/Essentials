package net.ess3.commands;

import org.bukkit.entity.Player;

import net.ess3.api.IUser;
import net.ess3.permissions.Permissions;


public class Commandenderchest extends EssentialsCommand
{

	@Override
	protected void run(final IUser user, final String commandLabel, final String[] args) throws Exception
	{	
		final Player player = user.getPlayer();
		if (args.length > 0 && Permissions.ENDERCHEST_OTHERS.isAuthorized(user))
		{
			final IUser invUser = ess.getUserMap().matchUserExcludingHidden(args[0], player);
			player.openInventory(invUser.getPlayer().getEnderChest());
			user.setEnderSee(true);
		}
		else
		{
			player.openInventory(player.getEnderChest());
			user.setEnderSee(false);
		}

	}
}
