package net.ess3.commands;

import static net.ess3.I18n._;
import net.ess3.api.IUser;
import net.ess3.permissions.Permissions;
import net.ess3.permissions.UnlimitedItemPermissions;
import java.util.Locale;
import java.util.Set;
import lombok.Cleanup;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;


public class Commandunlimited extends EssentialsCommand
{
	@Override
	public void run(final IUser user, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 1)
		{
			throw new NotEnoughArgumentsException();
		}

		@Cleanup
		IUser target = user;

		if (args.length > 1 && Permissions.UNLIMITED_OTHERS.isAuthorized(user))
		{
			target = ess.getUserMap().matchUser(args[1], false, false);
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
				toggleUnlimited(user, target, mat.toString());
				
			}
		}
		else
		{
			toggleUnlimited(user, target, args[0]);
		}
	}

	private String getList(final IUser target)
	{
		final StringBuilder output = new StringBuilder();
		output.append(_("unlimitedItems")).append(" ");
		boolean first = true;
		final Set<Material> items = target.getData().getUnlimited();
		if (items.isEmpty())
		{
			output.append(_("none"));
		}
		for (Material mater : items)
		{
			if (!first)
			{
				output.append(", ");
			}
			first = false;
			final String matname = mater.toString().toLowerCase(Locale.ENGLISH).replace("_", "");
			output.append(matname);
		}

		return output.toString();
	}

	private boolean toggleUnlimited(final IUser user, final IUser target, final String item) throws Exception
	{
		final ItemStack stack = ess.getItemDb().get(item, 1);
		stack.setAmount(Math.min(stack.getType().getMaxStackSize(), 2));

		final String itemname = stack.getType().toString().toLowerCase(Locale.ENGLISH).replace("_", "");
		if (!UnlimitedItemPermissions.getPermission(stack.getType()).isAuthorized(user))
		{
			throw new Exception(_("unlimitedItemPermission", itemname));
		}

		String message = "disableUnlimited";
		boolean enableUnlimited = false;
		if (!target.getData().hasUnlimited(stack.getType()))
		{
			message = "enableUnlimited";
			enableUnlimited = true;
			if (!target.getInventory().containsItem(true, true, stack))
			{
				target.getInventory().addItem(stack);
			}
		}

		if (user != target)
		{
			user.sendMessage(_(message, itemname, target.getPlayer().getDisplayName()));
		}
		target.sendMessage(_(message, itemname, target.getPlayer().getDisplayName()));
		target.acquireWriteLock();
		target.getData().setUnlimited(stack.getType(), enableUnlimited);

		return true;
	}
}
