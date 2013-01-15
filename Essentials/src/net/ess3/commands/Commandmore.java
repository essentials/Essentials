package net.ess3.commands;

import java.util.Locale;
import static net.ess3.I18n._;
import net.ess3.api.ChargeException;
import net.ess3.api.ISettings;
import net.ess3.api.IUser;
import net.ess3.economy.Trade;
import net.ess3.permissions.Permissions;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


public class Commandmore extends EssentialsCommand
{
	@Override
	public void run(final IUser user, final String commandLabel, final String[] args) throws Exception
	{
		ItemStack[] stacks;
		final Player player = user.getPlayer();
		if (args.length > 0 && args[0].equalsIgnoreCase("all"))
		{
			stacks = player.getInventory().getContents();
		}
		else
		{
			stacks = new ItemStack[]
			{
				player.getItemInHand()
			};
		}
		for (ItemStack stack : stacks)
		{
			if (stack == null)
			{
				if (stacks.length == 1)
				{
					throw new Exception(_("cantSpawnItem", "Air"));
				}
				else
				{
					continue;
				}
			}
			ISettings settings = ess.getSettings();

			final int defaultStackSize = settings.getData().getGeneral().getDefaultStacksize();
			final int oversizedStackSize = settings.getData().getGeneral().getOversizedStacksize();

			int newAmount = Permissions.OVERSIZEDSTACKS.isAuthorized(
					user) ? oversizedStackSize : defaultStackSize > 0 ? defaultStackSize : stack.getMaxStackSize();
			if (stack.getAmount() >= newAmount)
			{
				if (stacks.length == 1)
				{
					throw new NoChargeException();
				}
				else
				{
					continue;
				}
			}
			final String itemname = stack.getType().toString().toLowerCase(Locale.ENGLISH).replace("_", "");
			if (!Permissions.ITEMSPAWN.isAuthorized(user, stack))
			{
				if (stacks.length == 1)
				{
					throw new Exception(_("cantSpawnItem", itemname));
				}
				else
				{
					continue;
				}
			}
			if (stack.getAmount() < newAmount && stacks.length > 1)
			{
				Trade trade = new Trade("more", ess);
				try
				{
					trade.charge(user);
				}
				catch (ChargeException ex)
				{
					user.sendMessage(ex.getMessage());
					break;
				}
			}
			stack.setAmount(newAmount);
		}
		if (stacks.length > 1)
		{
			player.getInventory().setContents(stacks);
		}
		player.updateInventory();
		if (stacks.length > 1)
		{
			throw new NoChargeException();
		}
	}
}
