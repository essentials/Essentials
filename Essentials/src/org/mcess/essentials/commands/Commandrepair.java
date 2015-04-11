package org.mcess.essentials.commands;

import org.mcess.essentials.ChargeException;
import org.mcess.essentials.User;
import org.mcess.essentials.utils.StringUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import net.ess3.api.IUser;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.inventory.ItemStack;
import org.mcess.essentials.I18n;
import org.mcess.essentials.Trade;


public class Commandrepair extends EssentialsCommand
{
	public Commandrepair()
	{
		super("repair");
	}

	@Override
	public void run(final Server server, final User user, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 1 || args[0].equalsIgnoreCase("hand") || !user.isAuthorized("essentials.repair.all"))
		{
			repairHand(user);
		}
		else if (args[0].equalsIgnoreCase("all"))
		{
			final Trade charge = new Trade("repair-all", ess);
			charge.isAffordableFor(user);
			repairAll(user);
			charge.charge(user);
		}
		else
		{
			throw new NotEnoughArgumentsException();
		}
	}

	public void repairHand(User user) throws Exception
	{
		final ItemStack item = user.getBase().getItemInHand();
		if (item == null || item.getType().isBlock() || item.getDurability() == 0)
		{
			throw new Exception(I18n.tl("repairInvalidType"));
		}

		if (!item.getEnchantments().isEmpty()
			&& !ess.getSettings().getRepairEnchanted()
			&& !user.isAuthorized("essentials.repair.enchanted"))
		{
			throw new Exception(I18n.tl("repairEnchanted"));
		}

		final String itemName = item.getType().toString().toLowerCase(Locale.ENGLISH);
		final Trade charge = new Trade("repair-" + itemName.replace('_', '-'), new Trade("repair-" + item.getTypeId(), new Trade("repair-item", ess), ess), ess);

		charge.isAffordableFor(user);

		repairItem(item);

		charge.charge(user);
		user.getBase().updateInventory();
		user.sendMessage(I18n.tl("repair", itemName.replace('_', ' ')));
	}

	public void repairAll(User user) throws Exception
	{
		final List<String> repaired = new ArrayList<String>();
		repairItems(user.getBase().getInventory().getContents(), user, repaired);

		if (user.isAuthorized("essentials.repair.armor"))
		{
			repairItems(user.getBase().getInventory().getArmorContents(), user, repaired);
		}
		
		user.getBase().updateInventory();
		if (repaired.isEmpty())
		{
			throw new Exception(I18n.tl("repairNone"));
		}
		else
		{
			user.sendMessage(I18n.tl("repair", StringUtil.joinList(repaired)));
		}
	}

	private void repairItem(final ItemStack item) throws Exception
	{
		final Material material = Material.getMaterial(item.getTypeId());
		if (material.isBlock() || material.getMaxDurability() < 1)
		{
			throw new Exception(I18n.tl("repairInvalidType"));
		}

		if (item.getDurability() == 0)
		{
			throw new Exception(I18n.tl("repairAlreadyFixed"));
		}

		item.setDurability((short)0);
	}

	private void repairItems(final ItemStack[] items, final IUser user, final List<String> repaired)
	{
		for (ItemStack item : items)
		{
			if (item == null || item.getType().isBlock() || item.getDurability() == 0)
			{
				continue;
			}
			final String itemName = item.getType().toString().toLowerCase(Locale.ENGLISH);
			final Trade charge = new Trade("repair-" + itemName.replace('_', '-'), new Trade("repair-" + item.getTypeId(), new Trade("repair-item", ess), ess), ess);
			try
			{
				charge.isAffordableFor(user);
			}
			catch (ChargeException ex)
			{
				user.sendMessage(ex.getMessage());
				continue;
			}
			if (!item.getEnchantments().isEmpty()
				&& !ess.getSettings().getRepairEnchanted()
				&& !user.isAuthorized("essentials.repair.enchanted"))
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
