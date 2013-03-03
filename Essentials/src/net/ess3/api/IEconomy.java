package net.ess3.api;


public interface IEconomy extends IReload
{
	double getMoney(String name) throws UserDoesNotExistException;

	void setMoney(String name, double balance) throws UserDoesNotExistException, NoLoanPermittedException;

	void resetBalance(String name) throws UserDoesNotExistException, NoLoanPermittedException;

	String format(double amount);

	boolean playerExists(String name);

	boolean isNPC(String name) throws UserDoesNotExistException;

	boolean createNPC(String name);

	void removeNPC(String name) throws UserDoesNotExistException;
}
