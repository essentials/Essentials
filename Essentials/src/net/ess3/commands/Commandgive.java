package net.ess3.commands;

import java.util.Locale;
import static net.ess3.I18n._;
import net.ess3.api.IUser;
import net.ess3.permissions.Permissions;
import net.ess3.utils.Util;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;


public class Commandgive extends EssentialsCommand
{
	@Override
	protected void run(final CommandSender sender, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 2)
		{
			throw new NotEnoughArgumentsException();
		}

		final IUser giveTo = ess.getUserMap().matchUserExcludingHidden(args[0], getPlayerOrNull(sender));

		final ItemStack stack = ess.getItemDb().get(args[1], giveTo);

		final String itemname = stack.getType().toString().toLowerCase(Locale.ENGLISH).replace("_", "");
		if (!Permissions.GIVE.isAuthorized(sender, stack))
		{
			throw new Exception(_("cantSpawnItem", itemname));
		}

		if (args.length > 3 && Util.isInt(args[2]) && Util.isInt(args[3]))
		{
			stack.setAmount(Integer.parseInt(args[2]));
			stack.setDurability(Short.parseShort(args[3]));
		}
		else if (args.length > 2 && Integer.parseInt(args[2]) > 0)
		{
			stack.setAmount(Integer.parseInt(args[2]));
		}

		if (args.length > 3)
		{
			for (int i = Util.isInt(args[3]) ? 4 : 3; i < args.length; i++)
			{
				final String[] split = args[i].split("[:+',;.]", 2);
				if (split.length < 1)
				{
					continue;
				}
				final Enchantment enchantment = Commandenchant.getEnchantment(split[0], sender instanceof IUser ? (IUser)sender : null);
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

		giveTo.giveItems(stack, false);

		//TODO: TL this.
		final String itemName = stack.getType().toString().toLowerCase(Locale.ENGLISH).replace('_', ' ');
		sender.sendMessage(ChatColor.BLUE + "Giving " + stack.getAmount() + " of " + itemName + " to " + giveTo.getPlayer().getDisplayName() + ".");

	}
}
