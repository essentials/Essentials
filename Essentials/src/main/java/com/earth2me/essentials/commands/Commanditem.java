package com.earth2me.essentials.commands;

import static com.earth2me.essentials.I18n._;
import com.earth2me.essentials.MetaItemStack;
import com.earth2me.essentials.User;
import com.earth2me.essentials.craftbukkit.InventoryWorkaround;
import java.util.Locale;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.inventory.ItemStack;


public class Commanditem extends EssentialsCommand
{
	public Commanditem()
	{
		super("item");
	}

	@Override
	public void run(final Server server, final User user, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 1)
		{
			throw new NotEnoughArgumentsException();
		}
		ItemStack stack = ess.getItemDb().get(args[0]);

		final String itemname = stack.getType().toString().toLowerCase(Locale.ENGLISH).replace("_", "");
		if (ess.getSettings().permissionBasedItemSpawn()
			? (!user.isAuthorized("essentials.itemspawn.item-all")
			   && !user.isAuthorized("essentials.itemspawn.item-" + itemname)
			   && !user.isAuthorized("essentials.itemspawn.item-" + stack.getTypeId()))
			: (!user.isAuthorized("essentials.itemspawn.exempt")
			   && !user.canSpawnItem(stack.getTypeId())))
		{
			throw new Exception(_("cantSpawnItem", itemname));
		}
		try
		{
			if (args.length > 1 && Integer.parseInt(args[1]) > 0)
			{
				stack.setAmount(Integer.parseInt(args[1]));
			}
			else if (ess.getSettings().getDefaultStackSize() > 0)
			{
				stack.setAmount(ess.getSettings().getDefaultStackSize());
			}
			else if (ess.getSettings().getOversizedStackSize() > 0 && user.isAuthorized("essentials.oversizedstacks"))
			{
				stack.setAmount(ess.getSettings().getOversizedStackSize());
			}
		}
		catch (NumberFormatException e)
		{
			throw new NotEnoughArgumentsException();
		}
		if (args.length > 2)
		{
			MetaItemStack metaStack = new MetaItemStack(stack);
			final boolean allowUnsafe = ess.getSettings().allowUnsafeEnchantments() && user.isAuthorized("essentials.enchantments.allowunsafe");
			
			metaStack.parseStringMeta(user.getSource(), allowUnsafe, args, 2, ess);
			
			stack = metaStack.getItemStack();
		}


		if (stack.getType() == Material.AIR)
		{
			throw new Exception(_("cantSpawnItem", "Air"));
		}

		final String displayName = stack.getType().toString().toLowerCase(Locale.ENGLISH).replace('_', ' ');
		user.sendMessage(_("itemSpawn", stack.getAmount(), displayName));
		if (user.isAuthorized("essentials.oversizedstacks"))
		{
			InventoryWorkaround.addOversizedItems(user.getInventory(), ess.getSettings().getOversizedStackSize(), stack);
		}
		else
		{
			InventoryWorkaround.addItems(user.getInventory(), stack);
		}
		user.updateInventory();
	}
}
