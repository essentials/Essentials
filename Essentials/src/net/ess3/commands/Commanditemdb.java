package net.ess3.commands;

//TODO - replace alll the bukkit imports
import static net.ess3.I18n._;

import net.ess3.api.server.CommandSender;
import net.ess3.api.server.ItemStack;
import net.ess3.api.server.Material;
import net.ess3.api.server.Player;



public class Commanditemdb extends EssentialsCommand
{
	@Override
	protected void run(final CommandSender sender, final String commandLabel, final String[] args) throws Exception
	{
		ItemStack itemStack = null;
		if (args.length < 1)
		{
			if (sender instanceof Player)
			{
				itemStack = ((Player)sender).getItemInHand();
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


		if (itemStack.getType() != Material.AIR)
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
