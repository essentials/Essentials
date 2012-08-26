package net.ess3.commands;

import static net.ess3.I18n._;
import net.ess3.api.IUser;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;



public class Commandsetworth extends EssentialsCommand
{
	@Override
	public void run(final IUser user, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 2)
		{
			throw new NotEnoughArgumentsException();
		}

		ItemStack stack;
		String price;

		if (args.length == 1)
		{
			stack = user.getInventory().getItemInHand();
			price = args[0];
		}
		else
		{
			stack = ess.getItemDb().get(args[0]);
			price = args[1];
		}

		ess.getWorth().setPrice(stack, Double.parseDouble(price));
		user.sendMessage(_("worthSet"));
	}	

	@Override
	protected void run(final CommandSender sender, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 2)
		{
			throw new NotEnoughArgumentsException();
		}

		ItemStack stack = ess.getItemDb().get(args[0]);
		ess.getWorth().setPrice(stack, Double.parseDouble(args[1]));
		sender.sendMessage(_("worthSet"));
	}
}
