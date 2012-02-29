package com.earth2me.essentials.components.commands.handlers;

import com.earth2me.essentials.components.users.IUserComponent;
import static com.earth2me.essentials.components.i18n.I18nComponent.$;
import com.earth2me.essentials.api.ISettingsComponent;
import com.earth2me.essentials.components.commands.EssentialsCommand;
import com.earth2me.essentials.components.commands.NoChargeException;
import com.earth2me.essentials.perm.ItemPermissions;
import com.earth2me.essentials.perm.Permissions;
import java.util.Locale;
import org.bukkit.inventory.ItemStack;


public class Commandmore extends EssentialsCommand
{
	@Override
	public void run(final IUserComponent user, final String commandLabel, final String[] args) throws Exception
	{
		final ItemStack stack = user.getItemInHand();
		if (stack == null)
		{
			throw new Exception($("cantSpawnItem", "Air"));
		}
		int defaultStackSize = 0;
		int oversizedStackSize = 0;
		ISettingsComponent settings = getContext().getSettings();
		settings.acquireReadLock();
		try
		{
			defaultStackSize = settings.getData().getGeneral().getDefaultStacksize();
			oversizedStackSize = settings.getData().getGeneral().getOversizedStacksize();
		}
		finally
		{
			settings.unlock();
		}
		if (stack.getAmount() >= (Permissions.OVERSIZEDSTACKS.isAuthorized(user)
								  ? oversizedStackSize
								  : defaultStackSize > 0 ? defaultStackSize : stack.getMaxStackSize()))
		{
			throw new NoChargeException();
		}
		final String itemname = stack.getType().toString().toLowerCase(Locale.ENGLISH).replace("_", "");
		if (!ItemPermissions.getPermission(stack.getType()).isAuthorized(user))
		{
			throw new Exception($("cantSpawnItem", itemname));
		}
		if (Permissions.OVERSIZEDSTACKS.isAuthorized(user))
		{
			stack.setAmount(oversizedStackSize);
		}
		else
		{
			stack.setAmount(defaultStackSize > 0 ? defaultStackSize : stack.getMaxStackSize());
		}
		user.updateInventory();
	}
}