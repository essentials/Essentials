package com.earth2me.essentials.components.commands.handlers;

import com.earth2me.essentials.components.users.IUserComponent;
import com.earth2me.essentials.components.commands.EssentialsCommand;
import com.earth2me.essentials.components.commands.NotEnoughArgumentsException;
import static com.earth2me.essentials.components.i18n.I18nComponent.$;
import com.earth2me.essentials.perm.Permissions;
import java.util.List;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class Commandclearinventory extends EssentialsCommand
{
	//TODO: Cleanup
	@Override
	public void run(final IUserComponent user, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length > 0 && Permissions.CLEARINVENTORY_OTHERS.isAuthorized(user))
		{
			//TODO: Fix fringe user match case.
			if (args[0].length() >= 3)
			{
				List<Player> online = getServer().matchPlayer(args[0]);

				if (!online.isEmpty())
				{
					for (Player p : online)
					{
						p.getInventory().clear();
						user.sendMessage($("inventoryClearedOthers", p.getDisplayName()));
					}
					return;
				}
				throw new Exception($("playerNotFound"));
			}
			else
			{
				Player p = getServer().getPlayer(args[0]);
				if (p != null)
				{
					p.getInventory().clear();
					user.sendMessage($("inventoryClearedOthers", p.getDisplayName()));
				}
				else
				{
					throw new Exception($("playerNotFound"));
				}
			}
		}
		else
		{
			user.getInventory().clear();
			user.sendMessage($("inventoryCleared"));
		}
	}

	@Override
	protected void run(final CommandSender sender, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 1)
		{
			throw new NotEnoughArgumentsException();
		}

		if (args[0].length() >= 3)
		{
			List<Player> online = getServer().matchPlayer(args[0]);

			if (!online.isEmpty())
			{
				for (Player p : online)
				{
					p.getInventory().clear();
					sender.sendMessage($("inventoryClearedOthers", p.getDisplayName()));
				}
				return;
			}
			throw new Exception($("playerNotFound"));
		}
		else
		{
			Player u = getServer().getPlayer(args[0]);
			if (u != null)
			{
				u.getInventory().clear();
				sender.sendMessage($("inventoryClearedOthers", u.getDisplayName()));
			}
			else
			{
				throw new Exception($("playerNotFound"));
			}
		}
	}
}
