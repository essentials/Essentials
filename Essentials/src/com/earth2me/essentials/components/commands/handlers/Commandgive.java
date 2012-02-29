package com.earth2me.essentials.components.commands.handlers;

import com.earth2me.essentials.components.users.IUserComponent;
import com.earth2me.essentials.components.commands.EssentialsCommand;
import com.earth2me.essentials.components.commands.NotEnoughArgumentsException;
import com.earth2me.essentials.perm.GivePermissions;
import java.util.Locale;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


public class Commandgive extends EssentialsCommand
{
	//TODO: move these messages to message file
	@Override
	public void run(final CommandSender sender, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 2)
		{
			throw new NotEnoughArgumentsException();
		}

		final IUserComponent giveTo = getPlayer(args, 0);

		final ItemStack stack = getContext().getItems().get(args[1], giveTo);

		final String itemname = stack.getType().toString().toLowerCase(Locale.ENGLISH).replace("_", "");
		if (!GivePermissions.getPermission(stack.getType()).isAuthorized(sender))
		{
			throw new Exception(ChatColor.RED + "You are not allowed to spawn the item " + itemname);
		}

		if (args.length > 2 && Integer.parseInt(args[2]) > 0)
		{
			stack.setAmount(Integer.parseInt(args[2]));
		}

		if (args.length > 3)
		{
			for (int i = 3; i < args.length; i++)
			{
				final String[] split = args[i].split("[:+',;.]", 2);
				if (split.length < 1)
				{
					continue;
				}
				final Enchantment enchantment = Commandenchant.getEnchantment(split[0], sender instanceof Player ? getContext().getUser((Player)sender) : null);
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

		if (stack.getType() == Material.AIR)
		{
			throw new Exception(ChatColor.RED + "You can't give air.");
		}

		giveTo.giveItems(stack, false);

		final String itemName = stack.getType().toString().toLowerCase(Locale.ENGLISH).replace('_', ' ');
		sender.sendMessage(ChatColor.BLUE + "Giving " + stack.getAmount() + " of " + itemName + " to " + giveTo.getDisplayName() + ".");

	}
}
