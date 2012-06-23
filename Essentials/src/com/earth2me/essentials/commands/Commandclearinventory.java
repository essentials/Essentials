package com.earth2me.essentials.commands;

import static com.earth2me.essentials.I18n._;
import com.earth2me.essentials.User;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


public class Commandclearinventory extends EssentialsCommand
{
	public Commandclearinventory()
	{
		super("clearinventory");
	}

	//TODO: Cleanup
	@Override
	public void run(Server server, User user, String commandLabel, String[] args) throws Exception
	{
		if (args.length > 0 && user.isAuthorized("essentials.clearinventory.others"))
		{
			//TODO: Fix fringe user match case.
			if (args[0].length() >= 3)
			{
				List<Player> online = server.matchPlayer(args[0]);

				if (!online.isEmpty())
				{
					for (Player p : online)
					{
						ItemStack clear = new ItemStack(Material.AIR, 1);
						p.getInventory().clear();
						p.getInventory().setBoots(clear);
						p.getInventory().setChestplate(clear);
						p.getInventory().setHelmet(clear);
						p.getInventory().setLeggings(clear);
						user.sendMessage(_("inventoryClearedOthers", p.getDisplayName()));
					}
					return;
				}
				throw new Exception(_("playerNotFound"));
			}
			else
			{
				Player p = server.getPlayer(args[0]);
				if (p != null)
				{
					p.getInventory().clear();
					p.getInventory().setBoots(null);
					p.getInventory().setChestplate(null);
					p.getInventory().setHelmet(null);
					p.getInventory().setLeggings(null);
					user.sendMessage(_("inventoryClearedOthers", p.getDisplayName()));
				}
				else
				{
					throw new Exception(_("playerNotFound"));
				}
			}
		}
		else
		{
			user.getInventory().clear();
			user.getInventory().setBoots(null);
			user.getInventory().setChestplate(null);
			user.getInventory().setHelmet(null);
			user.getInventory().setLeggings(null);
			user.sendMessage(_("inventoryCleared"));
		}
	}

	@Override
	protected void run(Server server, CommandSender sender, String commandLabel, String[] args) throws Exception
	{
		if (args.length < 1)
		{
			throw new NotEnoughArgumentsException();
		}

		if (args[0].length() >= 3)
		{
			List<Player> online = server.matchPlayer(args[0]);

			if (!online.isEmpty())
			{
				for (Player p : online)
				{
					p.getInventory().clear();
					p.getInventory().setBoots(null);
					p.getInventory().setChestplate(null);
					p.getInventory().setHelmet(null);
					p.getInventory().setLeggings(null);
					sender.sendMessage(_("inventoryClearedOthers", p.getDisplayName()));
				}
				return;
			}
			throw new Exception(_("playerNotFound"));
		}
		else
		{
			Player u = server.getPlayer(args[0]);
			if (u != null)
			{
				u.getInventory().clear();
				u.getInventory().setBoots(null);
				u.getInventory().setChestplate(null);
				u.getInventory().setHelmet(null);
				u.getInventory().setLeggings(null);
				sender.sendMessage(_("inventoryClearedOthers", u.getDisplayName()));
			}
			else
			{
				throw new Exception(_("playerNotFound"));
			}
		}
	}
}
