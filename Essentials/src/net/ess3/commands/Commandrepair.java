package net.ess3.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import static net.ess3.I18n._;
import net.ess3.api.ChargeException;
import net.ess3.api.IUser;
import net.ess3.economy.Trade;
import net.ess3.permissions.Permissions;
import net.ess3.utils.Util;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;


public class Commandrepair extends EssentialsCommand
{
	@Override
	public void run(final IUser user, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 1)
		{
			throw new NotEnoughArgumentsException();
		}

		if (args[0].equalsIgnoreCase("hand"))
		{
			final ItemStack item = user.getPlayer().getItemInHand();
			if (item == null)
			{
				throw new Exception(_("repairInvalidType"));
			}

			if (!item.getEnchantments().isEmpty() && !Permissions.REPAIR_ENCHANTED.isAuthorized(user))
			{
				throw new Exception(_("repairEnchanted"));
			}

			final String itemName = item.getType().toString().toLowerCase(Locale.ENGLISH);
			final Trade charge = new Trade("repair-" + itemName.replace('_', '-'), ess);

			charge.isAffordableFor(user);

			repairItem(item);

			charge.charge(user);

			user.sendMessage(_("repair", itemName.replace('_', ' ')));
		}
		else if (args[0].equalsIgnoreCase("all") && Permissions.REPAIR_ALL.isAuthorized(user))
		{
			final Trade charge = new Trade("repair-all", ess);
			charge.isAffordableFor(user);
			final List<String> repaired = new ArrayList<String>();
			repairItems(user.getPlayer().getInventory().getContents(), user, repaired);

			if (Permissions.REPAIR_ARMOR.isAuthorized(user))
			{
				repairItems(user.getPlayer().getInventory().getArmorContents(), user, repaired);
			}

			if (repaired.isEmpty())
			{
				throw new Exception(_("repairNone"));
			}
			else
			{
				user.sendMessage(_("repair", Util.joinList(repaired)));
			}
			charge.charge(user);

		}
		else
		{
			throw new NotEnoughArgumentsException();
		}
	}

	private void repairItem(final ItemStack item) throws Exception
	{
		final Material material = Material.getMaterial(item.getTypeId());
		if (material.isBlock() || material.getMaxDurability() < 1)
		{
			throw new Exception(_("repairInvalidType"));
		}

		if (item.getDurability() == 0)
		{
			throw new Exception(_("repairAlreadyFixed"));
		}

		item.setDurability((short)0);
	}

	private void repairItems(final ItemStack[] items, final IUser user, final List<String> repaired)
	{
		for (ItemStack item : items)
		{
			if (item == null)
			{
				continue;
			}
			final String itemName = item.getType().toString().toLowerCase(Locale.ENGLISH);
			final Trade charge = new Trade("repair-" + itemName.replace('_', '-'), "repair-item", ess);
			try
			{
				charge.isAffordableFor(user);
			}
			catch (ChargeException ex)
			{
				user.sendMessage(ex.getMessage());
				continue;
			}
			if (!item.getEnchantments().isEmpty() && !Permissions.REPAIR_ENCHANTED.isAuthorized(user))
			{
				continue;
			}

			try
			{
				repairItem(item);
			}
			catch (Exception e)
			{
				continue;
			}
			try
			{
				charge.charge(user);
			}
			catch (ChargeException ex)
			{
				user.sendMessage(ex.getMessage());
			}
			repaired.add(itemName.replace('_', ' '));
		}
	}
}
