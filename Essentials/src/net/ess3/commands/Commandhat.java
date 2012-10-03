package net.ess3.commands;

import static net.ess3.I18n._;
import net.ess3.api.IUser;
import net.ess3.craftbukkit.InventoryWorkaround;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;



public class Commandhat extends EssentialsCommand
{
	@Override
	protected void run(IUser user, String commandLabel, String[] args) throws Exception
	{
		if (args.length > 0 && (args[0].contains("rem") || args[0].contains("off") || args[0].equalsIgnoreCase("0")))
		{
			final PlayerInventory inv = user.getPlayer().getInventory();
			final ItemStack head = inv.getHelmet();
			if (head == null || head.getType() == Material.AIR)
			{
				user.sendMessage(_("hatEmpty"));
			}
			else
			{
				final ItemStack air = new ItemStack(Material.AIR);
				inv.setHelmet(air);
				InventoryWorkaround.addItem(user.getPlayer().getInventory(), true, head);
				user.sendMessage(_("hatRemoved"));
			}
		}
		else
		{
			if (user.getPlayer().getItemInHand().getType() != Material.AIR)
			{
				final ItemStack hand = user.getPlayer().getItemInHand().clone();
				if (hand.getType().getMaxDurability() == 0)
				{
					final PlayerInventory inv = user.getPlayer().getInventory();
					final ItemStack head = inv.getHelmet();
					hand.setAmount(1);
					InventoryWorkaround.removeItem(inv, true, true, hand);
					inv.setHelmet(hand);
					inv.setItemInHand(head);
					user.sendMessage(_("hatPlaced"));
				}
				else
				{
					user.sendMessage(_("hatArmor"));
				}
			}
			else
			{
				user.sendMessage(_("hatFail"));
			}
		}
	}
}
