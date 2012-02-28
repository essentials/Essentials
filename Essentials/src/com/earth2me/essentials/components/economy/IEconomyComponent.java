package com.earth2me.essentials.components.economy;

import com.earth2me.essentials.components.IComponent;
import com.earth2me.essentials.components.settings.users.UserDoesNotExistException;


public interface IEconomyComponent extends IComponent
{
	public double getMoney(String name) throws UserDoesNotExistException;

	public void setMoney(String name, double balance) throws UserDoesNotExistException, NoLoanPermittedException;

	public void resetBalance(String name) throws UserDoesNotExistException, NoLoanPermittedException;

	public String format(double amount);

	public boolean playerExists(String name);

	public boolean isNpc(String name) throws UserDoesNotExistException;

	public boolean createNpc(String name);

	public void removeNpc(String name) throws UserDoesNotExistException;
}
