package com.earth2me.essentials.components.commands.handlers;

import static com.earth2me.essentials.I18nComponent._;
import com.earth2me.essentials.Util;
import com.earth2me.essentials.components.commands.EssentialsCommand;
import com.earth2me.essentials.components.commands.NotEnoughArgumentsException;
import com.earth2me.essentials.components.users.IUser;
import java.util.Locale;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;


public class Commandworth extends EssentialsCommand
{
	//TODO: Remove duplication
	@Override
	public void run(final IUser user, final String commandLabel, final String[] args) throws Exception
	{
		ItemStack iStack = user.getInventory().getItemInHand();
		int amount = iStack.getAmount();

		if (args.length > 0)
		{
			iStack = getContext().getItems().get(args[0]);
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
		final double worth = getContext().getWorths().getPrice(iStack);
		if (Double.isNaN(worth))
		{
			throw new Exception(_("itemCannotBeSold"));
		}

		user.sendMessage(iStack.getDurability() != 0
						 ? _("worthMeta",
							 iStack.getType().toString().toLowerCase(Locale.ENGLISH).replace("_", ""),
							 iStack.getDurability(),
							 Util.formatCurrency(worth * amount, getContext()),
							 amount,
							 Util.formatCurrency(worth, getContext()))
						 : _("worth",
							 iStack.getType().toString().toLowerCase(Locale.ENGLISH).replace("_", ""),
							 Util.formatCurrency(worth * amount, getContext()),
							 amount,
							 Util.formatCurrency(worth, getContext())));
	}

	@Override
	protected void run(final CommandSender sender, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 1)
		{
			throw new NotEnoughArgumentsException();
		}

		ItemStack iStack = getContext().getItems().get(args[0]);
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
		final double worth = getContext().getWorths().getPrice(iStack);
		if (Double.isNaN(worth))
		{
			throw new Exception(_("itemCannotBeSold"));
		}

		sender.sendMessage(iStack.getDurability() != 0
						   ? _("worthMeta",
							   iStack.getType().toString().toLowerCase(Locale.ENGLISH).replace("_", ""),
							   iStack.getDurability(),
							   Util.formatCurrency(worth * amount, getContext()),
							   amount,
							   Util.formatCurrency(worth, getContext()))
						   : _("worth",
							   iStack.getType().toString().toLowerCase(Locale.ENGLISH).replace("_", ""),
							   Util.formatCurrency(worth * amount, getContext()),
							   amount,
							   Util.formatCurrency(worth, getContext())));

	}
}
