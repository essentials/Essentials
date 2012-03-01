package com.earth2me.essentials.components.economy;

import com.earth2me.essentials.Util;
import com.earth2me.essentials.api.IContext;
import com.earth2me.essentials.api.ISettingsComponent;
import com.earth2me.essentials.components.Component;
import com.earth2me.essentials.components.settings.money.MoneyComponent;
import com.earth2me.essentials.components.users.IUserComponent;
import com.earth2me.essentials.components.users.UserDoesNotExistException;
import com.earth2me.essentials.perm.Permissions;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class EconomyComponent extends Component implements IEconomyComponent
{
	private final MoneyComponent npcsMoney;

	public EconomyComponent(final IContext context)
	{
		super(context);

		this.npcsMoney = new MoneyComponent(context, context.getEssentials());
	}

	@Override
	public void onEnable()
	{
		super.onEnable();

		getContext().getEssentials().add(npcsMoney);
	}

	@Override
	public void close()
	{
		getContext().getEssentials().remove(npcsMoney);

		super.close();
	}

	private double getNpcBalance(final String name) throws UserDoesNotExistException
	{
		npcsMoney.acquireReadLock();
		try
		{
			final Map<String, Double> balances = npcsMoney.getData().getBalances();
			if (balances == null)
			{
				throw new UserDoesNotExistException(name);
			}
			final Double balance = npcsMoney.getData().getBalances().get(name.toLowerCase(Locale.ENGLISH));
			if (balance == null)
			{
				throw new UserDoesNotExistException(name);
			}
			return balance;
		}
		finally
		{
			npcsMoney.unlock();
		}
	}

	private void setNpcBalance(final String name, final double balance, final boolean checkExistance) throws UserDoesNotExistException
	{
		npcsMoney.acquireWriteLock();
		try
		{
			Map<String, Double> balances = npcsMoney.getData().getBalances();
			if (balances == null)
			{
				balances = new HashMap<String, Double>();
				npcsMoney.getData().setBalances(balances);
			}
			if (checkExistance && !balances.containsKey(name.toLowerCase(Locale.ENGLISH)))
			{
				throw new UserDoesNotExistException(name);
			}
			balances.put(name.toLowerCase(Locale.ENGLISH), balance);
		}
		finally
		{
			npcsMoney.unlock();
		}
	}

	private double getStartingBalance()
	{
		double startingBalance = 0;
		final ISettingsComponent settings = getContext().getSettings();
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
		npcsMoney.reload(false);

		super.reload();
	}

	@Override
	public double getMoney(final String name) throws UserDoesNotExistException
	{
		final IUserComponent user = getContext().getUser(name);
		if (user == null)
		{
			return getNpcBalance(name);
		}
		return user.getMoney();
	}

	@Override
	public void setMoney(final String name, final double balance) throws NoLoanPermittedException, UserDoesNotExistException
	{
		final IUserComponent user = getContext().getUser(name);
		if (user == null)
		{
			setNpcBalance(name, balance, true);
			return;
		}
		if (balance < 0.0 && !Permissions.ECO_LOAN.isAuthorized(user))
		{
			throw new NoLoanPermittedException();
		}
		user.setMoney(balance);
	}

	@Override
	public void resetBalance(final String name) throws NoLoanPermittedException, UserDoesNotExistException
	{
		setMoney(name, getStartingBalance());
	}

	@Override
	public String format(final double amount)
	{
		return Util.formatCurrency(amount, getContext());
	}

	@Override
	public boolean playerExists(final String name)
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
	public boolean isNpc(final String name) throws UserDoesNotExistException
	{
		final boolean result = getContext().getUser(name) == null;
		if (result)
		{
			getNpcBalance(name);
		}
		return result;
	}

	@Override
	public boolean createNpc(final String name)
	{
		try
		{
			if (isNpc(name))
			{

				setNpcBalance(name, getStartingBalance(), false);
				return true;
			}
		}
		catch (UserDoesNotExistException ex)
		{
			try
			{
				setNpcBalance(name, getStartingBalance(), false);
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
	public void removeNpc(final String name) throws UserDoesNotExistException
	{
		npcsMoney.acquireWriteLock();
		try
		{
			Map<String, Double> balances = npcsMoney.getData().getBalances();
			if (balances == null)
			{
				balances = new HashMap<String, Double>();
				npcsMoney.getData().setBalances(balances);
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
			npcsMoney.unlock();
		}
	}
}
