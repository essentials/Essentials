package com.earth2me.essentials.components.commands.handlers;

import com.earth2me.essentials.components.users.IUserComponent;
import com.earth2me.essentials.Enchantments;
import static com.earth2me.essentials.components.i18n.I18nComponent.$;
import com.earth2me.essentials.Util;
import com.earth2me.essentials.components.commands.EssentialsCommand;
import com.earth2me.essentials.components.commands.NotEnoughArgumentsException;
import com.earth2me.essentials.perm.EnchantPermissions;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;


public class Commandenchant extends EssentialsCommand
{
	//TODO: Implement charge costs: final Trade charge = new Trade("enchant-" + enchantmentName, ess);
	@Override
	protected void run(final IUserComponent user, final String commandLabel, final String[] args) throws Exception
	{
		final ItemStack stack = user.getItemInHand();
		if (stack == null)
		{
			throw new Exception($("nothingInHand"));
		}
		if (args.length == 0)
		{
			final Set<String> enchantmentslist = new TreeSet<String>();
			for (Map.Entry<String, Enchantment> entry : Enchantments.entrySet())
			{
				final String enchantmentName = entry.getValue().getName().toLowerCase(Locale.ENGLISH);
				if (enchantmentslist.contains(enchantmentName) || EnchantPermissions.getPermission(enchantmentName).isAuthorized(user))
				{
					enchantmentslist.add(entry.getKey());
					//enchantmentslist.add(enchantmentName);
				}
			}
			throw new NotEnoughArgumentsException($("enchantments", Util.joinList(enchantmentslist.toArray())));
		}
		int level = -1;
		if (args.length > 1)
		{
			try
			{
				level = Integer.parseInt(args[1]);
			}
			catch (NumberFormatException ex)
			{
				level = -1;
			}
		}
		final Enchantment enchantment = getEnchantment(args[0], user);
		if (level < 0 || level > enchantment.getMaxLevel())
		{
			level = enchantment.getMaxLevel();
		}
		if (level == 0)
		{
			stack.removeEnchantment(enchantment);
		}
		else
		{
			stack.addEnchantment(enchantment, level);
		}
		user.getInventory().setItemInHand(stack);
		user.updateInventory();
		final String enchantmentName = enchantment.getName().toLowerCase(Locale.ENGLISH);
		if (level == 0)
		{
			user.sendMessage($("enchantmentRemoved", enchantmentName.replace('_', ' ')));
		}
		else
		{
			user.sendMessage($("enchantmentApplied", enchantmentName.replace('_', ' ')));
		}
	}

	public static Enchantment getEnchantment(final String name, final IUserComponent user) throws Exception
	{

		final Enchantment enchantment = Enchantments.getByName(name);
		if (enchantment == null)
		{
			throw new Exception($("enchantmentNotFound"));
		}
		final String enchantmentName = enchantment.getName().toLowerCase(Locale.ENGLISH);
		if (user != null && !EnchantPermissions.getPermission(enchantmentName).isAuthorized(user))
		{
			throw new Exception($("enchantmentPerm", enchantmentName));
		}
		return enchantment;
	}
}
