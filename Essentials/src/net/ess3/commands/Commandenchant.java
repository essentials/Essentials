package net.ess3.commands;

import static net.ess3.I18n._;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.Material;
import net.ess3.api.IUser;
import net.ess3.bukkit.Enchantments;
import net.ess3.permissions.Permissions;
import net.ess3.utils.Util;


public class Commandenchant extends EssentialsCommand
{
	//TODO: Implement charge costs: final Trade charge = new Trade("enchant-" + enchantmentName, ess);
	@Override
	protected void run(final IUser user, final String commandLabel, final String[] args) throws Exception
	{
		final ItemStack stack = user.getPlayer().getItemInHand();
		if (stack == null)
		{
			throw new Exception(_("nothingInHand"));
		}
		if (args.length == 0)
		{
			final Set<String> enchantmentslist = new TreeSet<String>();
			for (Map.Entry<String, Enchantment> entry : Enchantments.entrySet())
			{
				final String enchantmentName = entry.getValue().getName().toLowerCase(Locale.ENGLISH);
				if (enchantmentslist.contains(enchantmentName) || Permissions.ENCHANT.isAuthorized(user, enchantmentName))
				{
					enchantmentslist.add(entry.getKey());
					//enchantmentslist.add(enchantmentName);
				}
			}
			throw new NotEnoughArgumentsException(_("enchantments", Util.joinList(enchantmentslist.toArray())));
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
		final boolean allowUnsafe = Permissions.ENCHANT_UNSAFE.isAuthorized(user);
		final Enchantment enchantment = getEnchantment(args[0], user);
		if (level < 0 || (!allowUnsafe && level > enchantment.getMaxLevel()))
		{
			level = enchantment.getMaxLevel();
		}
		
		
		if (stack.getType().equals(Material.ENCHANTED_BOOK))
		{
			EnchantmentStorageMeta meta = (EnchantmentStorageMeta) stack.getItemMeta();
			
			if (level == 0)
			{
				if (meta.hasStoredEnchant(enchantment))
				{
					meta.removeStoredEnchant(enchantment);
					stack.setItemMeta(meta);
				}
			}
			else
			{
				// Enchanted Books only allowed to have one enchantment
				if (meta.hasStoredEnchants())
				{
					// Although there should be only one, don't make assumptions
					Iterator<Map.Entry<Enchantment, Integer>> entries = meta.getStoredEnchants().entrySet().iterator();
					while (entries.hasNext())
					{
						Map.Entry<Enchantment, Integer> entry = entries.next();
						Enchantment ench = entry.getKey();
						meta.removeStoredEnchant(ench);
					}
				}
				
				meta.addStoredEnchant(enchantment, level, allowUnsafe);
				stack.setItemMeta(meta);
			}
		}
		else // any other material beside Enchanted Book
		{
			if (level == 0)
			{
				stack.removeEnchantment(enchantment);
			}
			else
			{
				if (allowUnsafe)
				{
					stack.addUnsafeEnchantment(enchantment, level);
				}
				else
				{
					stack.addEnchantment(enchantment, level);
				}
			}
		}
		
		final Player player = user.getPlayer();
		player.getInventory().setItemInHand(stack);
		player.updateInventory();
		final String enchantmentName = enchantment.getName().toLowerCase(Locale.ENGLISH);
		if (level == 0)
		{
			user.sendMessage(_("enchantmentRemoved", enchantmentName.replace('_', ' ')));
		}
		else
		{
			user.sendMessage(_("enchantmentApplied", enchantmentName.replace('_', ' ')));
		}
	}

	public static Enchantment getEnchantment(final String name, final IUser user) throws Exception
	{

		final Enchantment enchantment = Enchantments.getByName(name);
		if (enchantment == null)
		{
			throw new Exception(_("enchantmentNotFound"));
		}
		final String enchantmentName = enchantment.getName().toLowerCase(Locale.ENGLISH);
		if (user != null && !Permissions.ENCHANT.isAuthorized(user, enchantmentName))
		{
			throw new Exception(_("enchantmentPerm", enchantmentName));
		}
		return enchantment;
	}
}
