package com.earth2me.essentials.components.economy;

import com.earth2me.essentials.components.IComponent;
import com.earth2me.essentials.components.users.UserDoesNotExistException;


public interface IEconomyComponent extends IComponent
{
	double getMoney(String name) throws UserDoesNotExistException;

	void setMoney(String name, double balance) throws UserDoesNotExistException, NoLoanPermittedException;

	void resetBalance(String name) throws UserDoesNotExistException, NoLoanPermittedException;

	String format(double amount);

	boolean playerExists(String name);

	boolean isNpc(String name) throws UserDoesNotExistException;

	boolean createNpc(String name);

	void removeNpc(String name) throws UserDoesNotExistException;
}
