package net.ess3.economy;

import static net.ess3.I18n._;
import net.ess3.api.ChargeException;
import net.ess3.api.IEssentials;
import net.ess3.api.ISettings;
import net.ess3.api.IUser;
import net.ess3.api.server.ItemStack;
import net.ess3.api.server.Location;
import net.ess3.permissions.NoCommandCostPermissions;
import net.ess3.permissions.Permissions;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.Cleanup;



public class Trade
{
	private final transient String command;
	private final transient Double money;
	private final transient ItemStack itemStack;
	private final transient Integer exp;
	private final transient IEssentials ess;

	public Trade(final String command, final IEssentials ess)
	{
		this(command, null, null, null, ess);
	}

	public Trade(final double money, final IEssentials ess)
	{
		this(null, money, null, null, ess);
	}

	public Trade(final ItemStack items, final IEssentials ess)
	{
		this(null, null, items, null, ess);
	}

	public Trade(final int exp, final IEssentials ess)
	{
		this(null, null, null, exp, ess);
	}

	private Trade(final String command, final Double money, final ItemStack item, final Integer exp, final IEssentials ess)
	{
		this.command = command;
		this.money = money;
		this.itemStack = item;
		this.exp = exp;
		this.ess = ess;
	}

	public void isAffordableFor(final IUser user) throws ChargeException
	{
		final double mon = user.getMoney();
		if (getMoney() != null
			&& mon < getMoney()
			&& getMoney() > 0
			&& !Permissions.ECO_LOAN.isAuthorized(user))
		{
			throw new ChargeException(_("notEnoughMoney"));
		}

		if (getItemStack() != null
			&& !user.getInventory().containsItem(true, true, itemStack))
		{
			throw new ChargeException(_("missingItems", getItemStack().getAmount(), getItemStack().getType().toString().toLowerCase(Locale.ENGLISH).replace("_", " ")));
		}

		@Cleanup
		final ISettings settings = ess.getSettings();
		settings.acquireReadLock();

		if (command != null && !command.isEmpty()
			&& !NoCommandCostPermissions.getPermission(command).isAuthorized(user)
			&& mon < settings.getData().getEconomy().getCommandCost(command.charAt(0) == '/' ? command.substring(1) : command)
			&& 0 < settings.getData().getEconomy().getCommandCost(command.charAt(0) == '/' ? command.substring(1) : command)
			&& !Permissions.ECO_LOAN.isAuthorized(user))
		{
			throw new ChargeException(_("notEnoughMoney"));
		}

		if (exp != null && exp > 0
			&& user.getTotalExperience() < exp)
		{
			throw new ChargeException(_("notEnoughExperience"));
		}
	}

	public void pay(final IUser user)
	{
		pay(user, true);
	}

	public boolean pay(final IUser user, final boolean dropItems)
	{
		boolean success = true;
		if (getMoney() != null && getMoney() > 0)
		{
			user.giveMoney(getMoney());
		}
		if (getItemStack() != null)
		{
			if (dropItems)
			{
				final Map<Integer, ItemStack> leftOver = user.getInventory().addItem(true, getItemStack());
				final Location loc = user.getLocation();
				for (ItemStack dropStack : leftOver.values())
				{
					final int maxStackSize = dropStack.getType().getMaxStackSize();
					final int stacks = dropStack.getAmount() / maxStackSize;
					final int leftover = dropStack.getAmount() % maxStackSize;
					final ItemStack[] itemStacks = new ItemStack[stacks + (leftover > 0 ? 1 : 0)];
					for (int i = 0; i < stacks; i++)
					{
						final ItemStack stack = dropStack.clone();
						stack.setAmount(maxStackSize);
						itemStacks[i] = user.getWorld().dropItem(loc, stack);
					}
					if (leftover > 0)
					{
						final ItemStack stack = dropStack.clone();
						stack.setAmount(leftover);
						itemStacks[stacks] = user.getWorld().dropItem(loc, stack);
					}
				}
			}
			else
			{
				success = user.getInventory().addAllItems(true, getItemStack());
			}
			user.updateInventory();
		}
		if (getExperience() != null)
		{
			user.setTotalExperience(user.getTotalExperience() + getExperience());
		}
		return success;
	}

	public void charge(final IUser user) throws ChargeException
	{
		if (getMoney() != null)
		{
			if (!user.canAfford(getMoney()) && getMoney() > 0)
			{
				throw new ChargeException(_("notEnoughMoney"));
			}
			user.takeMoney(getMoney());
		}
		if (getItemStack() != null)
		{
			if (!user.getInventory().containsItem(true, true, itemStack))
			{
				throw new ChargeException(_("missingItems", getItemStack().getAmount(), getItemStack().getType().toString().toLowerCase(Locale.ENGLISH).replace("_", " ")));
			}
			user.getInventory().removeItem(true, true, getItemStack());
			user.updateInventory();
		}
		if (command != null && !command.isEmpty()
			&& !NoCommandCostPermissions.getPermission(command).isAuthorized(user))
		{
			@Cleanup
			final ISettings settings = ess.getSettings();
			settings.acquireReadLock();
			final double cost = settings.getData().getEconomy().getCommandCost(command.charAt(0) == '/' ? command.substring(1) : command);
			if (!user.canAfford(cost) && cost > 0)
			{
				throw new ChargeException(_("notEnoughMoney"));
			}
			user.takeMoney(cost);
		}
		if (getExperience() != null)
		{
			final int experience = user.getTotalExperience();
			if (experience < getExperience() && getExperience() > 0)
			{
				throw new ChargeException(_("notEnoughExperience"));
			}
			user.setTotalExperience(experience - getExperience());
		}
	}

	public Double getMoney()
	{
		return money;
	}

	public ItemStack getItemStack()
	{
		return itemStack;
	}

	public Integer getExperience()
	{
		return exp;
	}
	private static FileWriter fw = null;

	public static void log(String type, String subtype, String event, String sender, Trade charge, String receiver, Trade pay, Location loc, IEssentials ess)
	{
		@Cleanup
		final ISettings settings = ess.getSettings();
		settings.acquireReadLock();
		if (!settings.getData().getEconomy().isLogEnabled())
		{
			return;
		}
		if (fw == null)
		{
			try
			{
				fw = new FileWriter(new File(ess.getPlugin().getDataFolder(), "trade.log"), true);
			}
			catch (IOException ex)
			{
				Logger.getLogger("Minecraft").log(Level.SEVERE, null, ex);
			}
		}
		StringBuilder sb = new StringBuilder();
		sb.append(type).append(",").append(subtype).append(",").append(event).append(",\"");
		sb.append(DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL).format(new Date()));
		sb.append("\",\"");
		if (sender != null)
		{
			sb.append(sender);
		}
		sb.append("\",");
		if (charge == null)
		{
			sb.append("\"\",\"\",\"\"");
		}
		else
		{
			if (charge.getItemStack() != null)
			{
				sb.append(charge.getItemStack().getAmount()).append(",");
				sb.append(charge.getItemStack().getType().toString()).append(",");
				sb.append(charge.getItemStack().getDurability());
			}
			if (charge.getMoney() != null)
			{
				sb.append(charge.getMoney()).append(",");
				sb.append("money").append(",");
				sb.append(settings.getData().getEconomy().getCurrencySymbol());
			}
			if (charge.getExperience() != null)
			{
				sb.append(charge.getExperience()).append(",");
				sb.append("exp").append(",");
				sb.append("\"\"");
			}
		}
		sb.append(",\"");
		if (receiver != null)
		{
			sb.append(receiver);
		}
		sb.append("\",");
		if (pay == null)
		{
			sb.append("\"\",\"\",\"\"");
		}
		else
		{
			if (pay.getItemStack() != null)
			{
				sb.append(pay.getItemStack().getAmount()).append(",");
				sb.append(pay.getItemStack().getType().toString()).append(",");
				sb.append(pay.getItemStack().getDurability());
			}
			if (pay.getMoney() != null)
			{
				sb.append(pay.getMoney()).append(",");
				sb.append("money").append(",");
				sb.append(settings.getData().getEconomy().getCurrencySymbol());
			}
			if (pay.getExperience() != null)
			{
				sb.append(pay.getExperience()).append(",");
				sb.append("exp").append(",");
				sb.append("\"\"");
			}
		}
		if (loc == null)
		{
			sb.append(",\"\",\"\",\"\",\"\"");
		}
		else
		{
			sb.append(",\"");
			sb.append(loc.getWorld().getName()).append("\",");
			sb.append(loc.getBlockX()).append(",");
			sb.append(loc.getBlockY()).append(",");
			sb.append(loc.getBlockZ()).append(",");
		}
		sb.append("\n");
		try
		{
			fw.write(sb.toString());
			fw.flush();
		}
		catch (IOException ex)
		{
			Logger.getLogger("Minecraft").log(Level.SEVERE, null, ex);
		}
	}

	public static void closeLog()
	{
		if (fw != null)
		{
			try
			{
				fw.close();
			}
			catch (IOException ex)
			{
				Logger.getLogger("Minecraft").log(Level.SEVERE, null, ex);
			}
			fw = null;
		}
	}
}
