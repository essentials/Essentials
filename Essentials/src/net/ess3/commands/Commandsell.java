package net.ess3.commands;

import java.util.Locale;
import java.util.logging.Level;
import static net.ess3.I18n._;
import net.ess3.api.IUser;
import net.ess3.economy.Trade;
import net.ess3.utils.FormatUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


public class Commandsell extends EssentialsCommand
{
	@Override
	public void run(final IUser user, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 1)
		{
			throw new NotEnoughArgumentsException();
		}
		ItemStack is = null;
		if (args[0].equalsIgnoreCase("hand"))
		{
			is = user.getPlayer().getItemInHand();
		}
		else if (args[0].equalsIgnoreCase("inventory") || args[0].equalsIgnoreCase("invent") || args[0].equalsIgnoreCase("all"))
		{
			for (ItemStack stack : user.getPlayer().getInventory().getContents())
			{
				if (stack == null || stack.getType() == Material.AIR)
				{
					continue;
				}
				try
				{
					sellItem(user, stack, args, true);
				}
				catch (Exception e)
				{
				}
			}
			return;
		}
		else if (args[0].equalsIgnoreCase("blocks"))
		{
			for (ItemStack stack : user.getPlayer().getInventory().getContents())
			{
				if (stack == null || stack.getTypeId() > 255 || stack.getType() == Material.AIR)
				{
					continue;
				}
				try
				{
					sellItem(user, stack, args, true);
				}
				catch (Exception e)
				{
				}
			}
			return;
		}
		if (is == null)
		{
			is = ess.getItemDb().get(args[0]);
		}
		sellItem(user, is, args, false);
	}

	private void sellItem(IUser user, ItemStack is, String[] args, boolean isBulkSell) throws Exception
	{
		if (is == null || is.getType() == Material.AIR)
		{
			throw new Exception(_("itemSellAir"));
		}
		int amount = 0;
		if (args.length > 1)
		{
			amount = Integer.parseInt(args[1].replaceAll("[^0-9]", ""));
			if (args[1].startsWith("-"))
			{
				amount = -amount;
			}
		}
		final double worth = ess.getWorth().getPrice(is);
		final boolean stack = args.length > 1 && args[1].endsWith("s");

		if (Double.isNaN(worth))
		{
			throw new Exception(_("itemCannotBeSold"));
		}


		int max = 0;
		for (ItemStack s : user.getPlayer().getInventory().getContents())
		{
			if (s == null)
			{
				continue;
			}
			if (s.getTypeId() != is.getTypeId())
			{
				continue;
			}
			if (s.getDurability() != is.getDurability())
			{
				continue;
			}
			if (!s.getEnchantments().equals(is.getEnchantments()))
			{
				continue;
			}
			max += s.getAmount();
		}

		if (stack)
		{
			amount *= is.getType().getMaxStackSize();
		}
		if (amount < 1)
		{
			amount += max;
		}

		if (amount > max || amount < 1)
		{
			if (!isBulkSell)
			{
				user.sendMessage(_("itemNotEnough1"));
				user.sendMessage(_("itemNotEnough2"));
				throw new Exception(_("itemNotEnough3"));
			}
			else
			{
				return;
			}
		}

		final Player player = user.getPlayer();

		//TODO: Prices for Enchantments
		final ItemStack ris = is.clone();
		ris.setAmount(amount);
		player.getInventory().removeItem(ris);
		player.updateInventory();
		Trade.log("Command", "Sell", "Item", user.getName(), new Trade(ris, ess), user.getName(), new Trade(worth * amount, ess), player.getLocation(), ess);
		user.giveMoney(worth * amount);
		user.sendMessage(
				_(
				"itemSold", FormatUtil.displayCurrency(worth * amount, ess), amount, is.getType().toString().toLowerCase(Locale.ENGLISH),
				FormatUtil.displayCurrency(worth, ess)));
		logger.log(
				Level.INFO, _(
				"itemSoldConsole", player.getDisplayName(), is.getType().toString().toLowerCase(Locale.ENGLISH),
				FormatUtil.displayCurrency(worth * amount, ess), amount, FormatUtil.displayCurrency(worth, ess)));

	}
}
