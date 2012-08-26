package net.ess3.commands;

import java.util.Arrays;
import static net.ess3.I18n._;
import net.ess3.api.IUser;
import net.ess3.api.server.ItemStack;
import net.ess3.user.Inventory;



public class Commandinvsee extends EssentialsCommand
{
	@Override
	protected void run(final IUser user, final String commandLabel, final String[] args) throws Exception
	{

		if (args.length < 1 && user.getData().getInventory() == null)
		{
			throw new NotEnoughArgumentsException();
		}
		IUser invUser = user;
		if (args.length == 1)
		{
			invUser = ess.getUserMap().matchUser(args[0], false, false);
		}
		user.acquireWriteLock();
		if (invUser == user && user.getData().getInventory() != null)
		{
			invUser.getInventory().setContents(user.getData().getInventory().getBukkitInventory());
			user.getData().setInventory(null);
			user.sendMessage(_("invRestored"));
			throw new NoChargeException();
		}
		if (user.getData().getInventory() == null)
		{
			user.getData().setInventory(new Inventory(user.getInventory().getContents()));
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
