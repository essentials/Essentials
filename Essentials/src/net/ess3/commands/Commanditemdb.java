package net.ess3.commands;

import static net.ess3.I18n._;
import net.ess3.api.IUser;
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
		sender.sendMessage(itemStack.getType().toString() + "- " + itemStack.getTypeId() + ":" + Integer.toString(itemStack.getData().getData()));


		if (itemStack.getTypeId() != 0)
		{
			int maxuses = itemStack.getType().getMaxDurability();
			int durability = ((maxuses + 1) - itemStack.getDurability());
			if (maxuses != 0)
			{
				sender.sendMessage(_("durability", Integer.toString(durability)));
			}
		}
	}
}
