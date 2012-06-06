package com.earth2me.essentials.commands;

import static com.earth2me.essentials.I18n._;
import com.earth2me.essentials.api.IUser;
import com.earth2me.essentials.permissions.ItemPermissions;
import com.earth2me.essentials.permissions.Permissions;
import java.util.Locale;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;


public class Commanditem extends EssentialsCommand
{
	@Override
	public void run(final IUser user, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 1)
		{
			throw new NotEnoughArgumentsException();
		}
		final ItemStack stack = ess.getItemDb().get(args[0], user);

		final String itemname = stack.getType().toString().toLowerCase(Locale.ENGLISH).replace("_", "");
		if (!ItemPermissions.getPermission(stack.getType()).isAuthorized(user))
		{
			throw new Exception(_("cantSpawnItem", itemname));
		}

		try
		{
			if (args.length > 1 && Integer.parseInt(args[1]) > 0)
			{
				stack.setAmount(Integer.parseInt(args[1]));
			}
			else if (ess.getSettings().getData().getGeneral().getDefaultStacksize() > 0)
			{
				stack.setAmount(ess.getSettings().getData().getGeneral().getDefaultStacksize());
			}
			else if (ess.getSettings().getData().getGeneral().getOversizedStacksize()> 0 && Permissions.OVERSIZEDSTACKS.isAuthorized(user))
			{
				stack.setAmount(ess.getSettings().getData().getGeneral().getOversizedStacksize());
			}	
			if (args.length > 2)
			{
				for (int i = 2; i < args.length; i++)
				{
					final String[] split = args[i].split("[:+',;.]", 2);
					if (split.length < 1)
					{
						continue;
					}
					final Enchantment enchantment = Commandenchant.getEnchantment(split[0], user);
					int level;
					if (split.length > 1)
					{
						level = Integer.parseInt(split[1]);
					}
					else
					{
						level = enchantment.getMaxLevel();
					}
					stack.addEnchantment(enchantment, level);
				}
			}
		}
		catch (NumberFormatException e)
		{
			throw new NotEnoughArgumentsException();
		}

		if (stack.getType() == Material.AIR)
		{
			throw new Exception(_("cantSpawnItem", "Air"));
		}

		user.giveItems(stack, false);
		
		final String displayName = stack.getType().toString().toLowerCase(Locale.ENGLISH).replace('_', ' ');
		user.sendMessage(_("itemSpawn", stack.getAmount(), displayName));	
	}
}
