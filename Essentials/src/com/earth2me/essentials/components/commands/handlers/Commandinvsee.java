package com.earth2me.essentials.components.commands.handlers;

import com.earth2me.essentials.components.commands.EssentialsCommand;
import com.earth2me.essentials.components.commands.NoChargeException;
import com.earth2me.essentials.components.commands.NotEnoughArgumentsException;
import static com.earth2me.essentials.components.i18n.I18nComponent._;
import com.earth2me.essentials.components.users.IUserComponent;
import com.earth2me.essentials.components.users.Inventory;
import java.util.Arrays;
import org.bukkit.inventory.ItemStack;


public class Commandinvsee extends EssentialsCommand
{
	@Override
	protected void run(final IUserComponent user, final String commandLabel, final String[] args) throws Exception
	{

		if (args.length < 1 && user.getLastInventory() == null)
		{
			throw new NotEnoughArgumentsException();
		}
		IUserComponent invUser = user;
		if (args.length == 1)
		{
			invUser = getPlayer(args, 0);
		}
		user.acquireWriteLock();
		if (invUser == user && user.getLastInventory() != null)
		{
			invUser.getInventory().setContents(user.getLastInventory().getBukkitInventory());
			user.setLastInventory(null);
			user.sendMessage(_("invRestored"));
			throw new NoChargeException();
		}
		if (user.getLastInventory() == null)
		{
			user.setLastInventory(new Inventory(user.getInventory().getContents()));
		}
		ItemStack[] invUserStack = invUser.getInventory().getContents();
		final int userStackLength = user.getInventory().getContents().length;
		if (invUserStack.length < userStackLength)
		{
			invUserStack = Arrays.copyOf(invUserStack, userStackLength);
		}
		if (invUserStack.length > userStackLength)
		{
			throw new Exception(_("invBigger"));
		}
		user.getInventory().setContents(invUserStack);
		user.sendMessage(_("invSee", invUser.getDisplayName()));
		user.sendMessage(_("invSeeHelp"));
		throw new NoChargeException();
	}
}
