package com.earth2me.essentials.commands;

import static com.earth2me.essentials.I18n._;
import com.earth2me.essentials.api.IUser;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;


public class Commandhat extends EssentialsCommand
{
	@Override
	protected void run(IUser user, String commandLabel, String[] args) throws Exception
	{
		if (user.getItemInHand().getType() != Material.AIR)
		{
			final ItemStack hand = user.getItemInHand();
			if (hand.getType().getMaxDurability() == 0)
			{
				final PlayerInventory inv = user.getInventory();
				final ItemStack head = inv.getHelmet();
				inv.removeItem(hand);
				inv.setHelmet(hand);
				inv.setItemInHand(head);
				user.sendMessage(_("hatPlaced"));
			} else {
				user.sendMessage(_("hatArmor"));
			}
		}
		else
		{
			user.sendMessage(_("hatFail"));
		}
	}
}
