package net.ess3.commands;

import java.util.Locale;
import static net.ess3.I18n._;
import net.ess3.api.IUser;
import net.ess3.utils.FormatUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;


public class Commandworth extends EssentialsCommand
{
	//TODO: Remove duplication
	@Override
	public void run(final IUser user, final String commandLabel, final String[] args) throws Exception
	{
		ItemStack iStack = user.getPlayer().getItemInHand();
		int amount = iStack.getAmount();

		if (args.length > 0)
		{
			iStack = ess.getItemDb().get(args[0]);
		}

		try
		{
			if (args.length > 1)
			{
				amount = Integer.parseInt(args[1]);
			}
		}
		catch (NumberFormatException ex)
		{
			amount = iStack.getType().getMaxStackSize();
		}

		iStack.setAmount(amount);
		final double worth = ess.getWorth().getPrice(iStack);
		if (Double.isNaN(worth))
		{
			throw new Exception(_("itemCannotBeSold"));
		}

		user.sendMessage(
				iStack.getDurability() != 0 ? _(
						"worthMeta", iStack.getType().toString().toLowerCase(Locale.ENGLISH).replace("_", ""), iStack.getDurability(),
						FormatUtil.displayCurrency(worth * amount, ess), amount, FormatUtil.displayCurrency(worth, ess)) : _(
						"worth", iStack.getType().toString().toLowerCase(Locale.ENGLISH).replace("_", ""), FormatUtil.displayCurrency(worth * amount, ess),
						amount, FormatUtil.displayCurrency(worth, ess)));
	}

	@Override
	protected void run(final CommandSender sender, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 1)
		{
			throw new NotEnoughArgumentsException();
		}

		ItemStack iStack = ess.getItemDb().get(args[0]);
		int amount = iStack.getAmount();

		try
		{
			if (args.length > 1)
			{
				amount = Integer.parseInt(args[1]);
			}
		}
		catch (NumberFormatException ex)
		{
			amount = iStack.getType().getMaxStackSize();
		}

		iStack.setAmount(amount);
		final double worth = ess.getWorth().getPrice(iStack);
		if (Double.isNaN(worth))
		{
			throw new Exception(_("itemCannotBeSold"));
		}

		sender.sendMessage(
				iStack.getDurability() != 0 ? _(
						"worthMeta", iStack.getType().toString().toLowerCase(Locale.ENGLISH).replace("_", ""), iStack.getDurability(),
						FormatUtil.displayCurrency(worth * amount, ess), amount, FormatUtil.displayCurrency(worth, ess)) : _(
						"worth", iStack.getType().toString().toLowerCase(Locale.ENGLISH).replace("_", ""), FormatUtil.displayCurrency(worth * amount, ess),
						amount, FormatUtil.displayCurrency(worth, ess)));

	}
}
