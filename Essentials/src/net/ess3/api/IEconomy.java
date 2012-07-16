package net.ess3.api;


public interface IEconomy extends IReload
{
	public double getMoney(String name) throws UserDoesNotExistException;

	public void setMoney(String name, double balance) throws UserDoesNotExistException, NoLoanPermittedException;

	public void resetBalance(String name) throws UserDoesNotExistException, NoLoanPermittedException;

	public String format(double amount);

	public boolean playerExists(String name);

	public boolean isNPC(String name) throws UserDoesNotExistException;

	public boolean createNPC(String name);

	public void removeNPC(String name) throws UserDoesNotExistException;
}
