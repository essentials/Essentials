package com.earth2me.essentials.components.economy;

import com.earth2me.essentials.Util;
import com.earth2me.essentials.api.IContext;
import com.earth2me.essentials.api.ISettingsComponent;
import com.earth2me.essentials.components.Component;
import com.earth2me.essentials.components.users.IUser;
import com.earth2me.essentials.components.users.UserDoesNotExistException;
import com.earth2me.essentials.perm.Permissions;
import com.earth2me.essentials.components.settings.MoneyHolder;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class EconomyComponent extends Component implements IEconomyComponent
{
	private final MoneyHolder npcs;

	public EconomyComponent(IContext context)
	{
		super(context);
		this.npcs = new MoneyHolder(context);
	}

	private double getNPCBalance(String name) throws UserDoesNotExistException
	{
		npcs.acquireReadLock();
		try
		{
			Map<String, Double> balances = npcs.getData().getBalances();
			if (balances == null)
			{
				throw new UserDoesNotExistException(name);
			}
			Double balance = npcs.getData().getBalances().get(name.toLowerCase(Locale.ENGLISH));
			if (balance == null)
			{
				throw new UserDoesNotExistException(name);
			}
			return balance;
		}
		finally
		{
			npcs.unlock();
		}
	}

	private void setNPCBalance(String name, double balance, boolean checkExistance) throws UserDoesNotExistException
	{
		npcs.acquireWriteLock();
		try
		{
			Map<String, Double> balances = npcs.getData().getBalances();
			if (balances == null)
			{
				balances = new HashMap<String, Double>();
				npcs.getData().setBalances(balances);
			}
			if (checkExistance && !balances.containsKey(name.toLowerCase(Locale.ENGLISH)))
			{
				throw new UserDoesNotExistException(name);
			}
			balances.put(name.toLowerCase(Locale.ENGLISH), balance);
		}
		finally
		{
			npcs.unlock();
		}
	}

	private double getStartingBalance()
	{
		double startingBalance = 0;
		ISettingsComponent settings = getContext().getSettings();
		settings.acquireReadLock();
		try
		{
			startingBalance = settings.getData().getEconomy().getStartingBalance();
		}
		finally
		{
			settings.unlock();
		}
		return startingBalance;
	}

	@Override
	public void reload()
	{
		this.npcs.reload(false);
	}

	@Override
	public double getMoney(String name) throws UserDoesNotExistException
	{
		IUser user = getContext().getUser(name);
		if (user == null)
		{
			return getNPCBalance(name);
		}
		return user.getMoney();
	}

	@Override
	public void setMoney(String name, double balance) throws NoLoanPermittedException, UserDoesNotExistException
	{
		IUser user = getContext().getUser(name);
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
		return Util.formatCurrency(amount, getContext());
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
	public boolean isNpc(String name) throws UserDoesNotExistException
	{
		boolean result = getContext().getUser(name) == null;
		if (result)
		{
			getNPCBalance(name);
		}
		return result;
	}

	@Override
	public boolean createNpc(String name)
	{
		try
		{
			if (isNpc(name))
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
	public void removeNpc(String name) throws UserDoesNotExistException
	{
		npcs.acquireWriteLock();
		try
		{
			Map<String, Double> balances = npcs.getData().getBalances();
			if (balances == null)
			{
				balances = new HashMap<String, Double>();
				npcs.getData().setBalances(balances);
			}
			if (balances.containsKey(name.toLowerCase(Locale.ENGLISH)))
			{
				balances.remove(name.toLowerCase(Locale.ENGLISH));
			}
			else
			{
				throw new UserDoesNotExistException(name);
			}
		}
		finally
		{
			npcs.unlock();
		}
	}
}
