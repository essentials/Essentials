package com.earth2me.essentials.components.commands.handlers;

import com.earth2me.essentials.components.commands.EssentialsCommand;
import com.earth2me.essentials.components.commands.NotEnoughArgumentsException;
import static com.earth2me.essentials.components.i18n.I18nComponent.$;
import com.earth2me.essentials.components.users.IUserComponent;
import com.earth2me.essentials.craftbukkit.InventoryWorkaround;
import com.earth2me.essentials.perm.Permissions;
import com.earth2me.essentials.perm.UnlimitedItemPermissions;
import java.util.Locale;
import java.util.Set;
import lombok.Cleanup;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;


public class Commandunlimited extends EssentialsCommand
{
	@Override
	public void run(final IUserComponent user, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 1)
		{
			throw new NotEnoughArgumentsException();
		}

		@Cleanup
		IUserComponent target = user;

		if (args.length > 1 && Permissions.UNLIMITED_OTHERS.isAuthorized(user))
		{
			target = getPlayer(args, 1);
			target.acquireReadLock();
		}

		if (args[0].equalsIgnoreCase("list"))
		{
			final String list = getList(target);
			user.sendMessage(list);
		}
		else if (args[0].equalsIgnoreCase("clear"))
		{
			//TODO: Fix this, the clear should always work, even when the player does not have permission.
			final Set<Material> itemList = target.getData().getUnlimited();
			for(Material mat : itemList)
			{
				toggleUnlimited(user, target, mat.name());

			}
		}
		else
		{
			toggleUnlimited(user, target, args[0]);
		}
	}

	private String getList(final IUserComponent target)
	{
		final StringBuilder output = new StringBuilder();
		output.append($("unlimitedItems")).append(" ");
		boolean first = true;
		final Set<Material> items = target.getData().getUnlimited();
		if (items.isEmpty())
		{
			output.append($("none"));
		}
		for (Material mater : items)
		{
			if (!first)
			{
				output.append(", ");
			}
			first = false;
			final String matname = mater.name().toLowerCase(Locale.ENGLISH).replace("_", "");
			output.append(matname);
		}

		return output.toString();
	}

	private Boolean toggleUnlimited(final IUserComponent user, final IUserComponent target, final String item) throws Exception
	{
		final ItemStack stack = getContext().getItems().get(item, 1);
		stack.setAmount(Math.min(stack.getType().getMaxStackSize(), 2));

		final String itemname = stack.getType().toString().toLowerCase(Locale.ENGLISH).replace("_", "");
		if (!UnlimitedItemPermissions.getPermission(stack.getType()).isAuthorized(user))
		{
			throw new Exception($("unlimitedItemPermission", itemname));
		}

		String message = "disableUnlimited";
		Boolean enableUnlimited = false;
		if (!target.getData().hasUnlimited(stack.getType()))
		{
			message = "enableUnlimited";
			enableUnlimited = true;
			if (!InventoryWorkaround.containsItem(target.getInventory(), true, true, stack))
			{
				target.getInventory().addItem(stack);
			}
		}

		if (user != target)
		{
			user.sendMessage($(message, itemname, target.getDisplayName()));
		}
		target.sendMessage($(message, itemname, target.getDisplayName()));
		target.acquireWriteLock();
		target.getData().setUnlimited(stack.getType(), enableUnlimited);

		return true;
	}
}
