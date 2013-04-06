package net.ess3.commands;

import java.util.Arrays;
import static net.ess3.I18n._;
import net.ess3.api.IUser;
import net.ess3.user.Inventory;
import org.bukkit.inventory.ItemStack;


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
			invUser = ess.getUserMap().matchUserExcludingHidden(args[0], user.getPlayer());
		}
		if (invUser == user && user.getData().getInventory() != null)
		{
			invUser.getPlayer().getInventory().setContents(user.getData().getInventory().getBukkitInventory());
			user.getData().setInventory(null);
			user.queueSave();
			user.sendMessage(_("§6Your inventory has been restored."));
			throw new NoChargeException();
		}
		if (user.getData().getInventory() == null)
		{
			user.getData().setInventory(new Inventory(user.getPlayer().getInventory().getContents()));
			user.queueSave();
		}
		ItemStack[] invUserStack = invUser.getPlayer().getInventory().getContents();
		final int userStackLength = user.getPlayer().getInventory().getContents().length;
		if (invUserStack.length < userStackLength)
		{
			invUserStack = Arrays.copyOf(invUserStack, userStackLength);
		}
		if (invUserStack.length > userStackLength)
		{
			throw new Exception(_("§4The other users inventory is bigger than yours."));
		}
		user.getPlayer().getInventory().setContents(invUserStack);
		user.sendMessage(_("§6You see the inventory of§c {0}§6.", invUser.getPlayer().getDisplayName()));
		user.sendMessage(_("§6Use /invsee to restore your inventory."));
		throw new NoChargeException();
	}
}
