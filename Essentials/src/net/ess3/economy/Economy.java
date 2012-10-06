package net.ess3.economy;

import java.util.Locale;
import java.util.Map;
import net.ess3.api.*;
import net.ess3.permissions.Permissions;
import net.ess3.utils.FormatUtil;


public class Economy implements IEconomy
{
	private final IEssentials ess;
	private final MoneyHolder npcs;

	public Economy(IEssentials ess)
	{
		this.ess = ess;
		this.npcs = new MoneyHolder(ess);
	}

	private double getNPCBalance(String name) throws UserDoesNotExistException
	{
		Double balance = npcs.getData().getBalances().get(name.toLowerCase(Locale.ENGLISH));
		if (balance == null)
		{
			throw new UserDoesNotExistException(name);
		}
		return balance;
	}

	private void setNPCBalance(String name, double balance, boolean checkExistance) throws UserDoesNotExistException
	{
		Map<String, Double> balances = npcs.getData().getBalances();
		if (checkExistance && !balances.containsKey(name.toLowerCase(Locale.ENGLISH)))
		{
			throw new UserDoesNotExistException(name);
		}
		npcs.getData().setBalance(name.toLowerCase(Locale.ENGLISH), balance);
	}

	private double getStartingBalance()
	{
		return ess.getSettings().getData().getEconomy().getStartingBalance();
	}

	@Override
	public void onReload()
	{
		this.npcs.onReload(false);
	}

	@Override
	public double getMoney(String name) throws UserDoesNotExistException
	{
		IUser user = ess.getUserMap().getUser(name);
		if (user == null)
		{
			return getNPCBalance(name);
		}
		return user.getMoney();
	}

	@Override
	public void setMoney(String name, double balance) throws NoLoanPermittedException, UserDoesNotExistException
	{
		IUser user = ess.getUserMap().getUser(name);
		if (user == null)
		{
			setNPCBalance(name, balance, true);
			return;
		}
		if (balance < 0.0 && !Permissions.ECO_LOAN.isAuthorized(user))
		{
			throw new NoLoanPermittedException();
		}
		user.setMoney(balance);
	}

	@Override
	public void resetBalance(String name) throws NoLoanPermittedException, UserDoesNotExistException
	{
		setMoney(name, getStartingBalance());
	}

	@Override
	public String format(double amount)
	{
		return FormatUtil.displayCurrency(amount, ess);
	}

	@Override
	public boolean playerExists(String name)
	{
		try
		{
			getMoney(name);
			return true;
		}
		catch (UserDoesNotExistException ex)
		{
			return false;
		}
	}

	@Override
	public boolean isNPC(String name) throws UserDoesNotExistException
	{
		boolean result = ess.getUserMap().getUser(name) == null;
		if (result)
		{
			getNPCBalance(name);
		}
		return result;
	}

	@Override
	public boolean createNPC(String name)
	{
		try
		{
			if (isNPC(name))
			{

				setNPCBalance(name, getStartingBalance(), false);
				return true;
			}
		}
		catch (UserDoesNotExistException ex)
		{
			try
			{
				setNPCBalance(name, getStartingBalance(), false);
				return true;
			}
			catch (UserDoesNotExistException ex1)
			{
				//This should never happen!
			}
		}
		return false;
	}

	@Override
	public void removeNPC(String name) throws UserDoesNotExistException
	{
		Map<String, Double> balances = npcs.getData().getBalances();

		if (balances.containsKey(name.toLowerCase(Locale.ENGLISH)))
		{
			npcs.getData().removeBalance(name.toLowerCase(Locale.ENGLISH));
		}
		else
		{
			throw new UserDoesNotExistException(name);
		}
	}
}
