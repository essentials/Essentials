package net.ess3.commands;

import java.util.Locale;
import static net.ess3.I18n._;
import net.ess3.api.IUser;
import net.ess3.permissions.Permissions;
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
		if (args.length > 1 && "love".equals(args[0]) && "you".equals(args[1]))
		{	
			user.sendMessage("What is love?");
            user.sendMessage("Baby don't hurt me");
			return;
		}
		final ItemStack stack = ess.getItemDb().get(args[0], user);

		final String itemname = stack.getType().toString().toLowerCase(Locale.ENGLISH).replace("_", "");
		if (!Permissions.ITEMSPAWN.isAuthorized(user, stack))
		{
			throw new Exception(_("cantSpawnItem", itemname));
		}

		if (args.length > 1 && Integer.parseInt(args[1]) > 0)
		{
			stack.setAmount(Integer.parseInt(args[1]));
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
		
		if (stack.getTypeId() == 0)
		{
			throw new Exception(_("cantSpawnItem", "Air"));
		}

		user.giveItems(stack, false);
		
		final String displayName = stack.getType().toString().toLowerCase(Locale.ENGLISH).replace('_', ' ');
		user.sendMessage(_("itemSpawn", stack.getAmount(), displayName));	
	}
}
