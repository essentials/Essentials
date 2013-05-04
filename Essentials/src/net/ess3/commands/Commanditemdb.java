package net.ess3.commands;

import static net.ess3.I18n._;
import net.ess3.api.IUser;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;


public class Commanditemdb extends EssentialsCommand
{
	@Override
	protected void run(final CommandSender sender, final String commandLabel, final String[] args) throws Exception
	{
		ItemStack itemStack = null;
		if (args.length < 1)
		{
			if (sender instanceof IUser)
			{
				itemStack = ((IUser)sender).getPlayer().getItemInHand();
			}
			if (itemStack == null)
			{
				throw new NotEnoughArgumentsException();
			}
		}
		else
		{
			itemStack = ess.getItemDb().get(args[0]);
		}
		final Material itemType = itemStack.getType();
		final int id = itemType.getId();
		final String data = Integer.toString(itemStack.getData().getData());
		sender.sendMessage(_("ItemDatabaseResponse", itemStack.getType().toString(), id, data));
		if (id != 0)
		{
			final int maxuses = itemType.getMaxDurability();
			final int durability = ((maxuses + 1) - itemStack.getDurability());
			if (maxuses != 0)
			{
				sender.sendMessage(_("§6This tool has §c{0}§6 uses left", Integer.toString(durability)));
			}
		}
	}
}
