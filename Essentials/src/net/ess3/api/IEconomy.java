package net.ess3.api;


public interface IEconomy extends IReload
{
	/**
	 * Disregard women, acquire currency.
	 * Used to get the balance of a user
	 *
	 * @param name the name of the user
	 * @return the balance
	 * @throws UserDoesNotExistException thrown if the user does not exist
	 */
	double getMoney(String name) throws UserDoesNotExistException;

	/**
	 * Used to set the balance of a user
	 *
	 * @param name the name of the user
	 * @param balance the amount to set the balance to
	 * @throws UserDoesNotExistException thrown if the user does not exist
	 * @throws NoLoanPermittedException **TODO: this needs to be removed, due to changes in eco handling**
	 */
	void setMoney(String name, double balance) throws UserDoesNotExistException, NoLoanPermittedException;

	/**
	 * Used to reset the balance of a user to the starting balance (as defined in the config)
	 *
	 * @param name the name of the user
	 * @throws UserDoesNotExistException
	 * @throws NoLoanPermittedException **TODO: this needs to be removed, due to changes in eco handling**
	 */
	void resetBalance(String name) throws UserDoesNotExistException, NoLoanPermittedException;

	/**
	 * Used to format the balance as a string
	 *
	 * @param amount the balance to format
	 * @return the formatted string
	 */
	String format(double amount);

	/**
	 * Used to check if a user exists. A user exists if they have played on the server and have not had their user data deleted.
	 *
	 * @param name the name of the user to check
	 * @return true if the user exists, false if not
	 */
	boolean playerExists(String name);

	/**
	 * Used to check if the given user is a NPC. An example of a NPC would be a town or faction, which has a balance, but is not a player.
	 *
	 * @param name the name of the user to check
	 * @return true if the user is a NPC, false if not
	 * @throws UserDoesNotExistException thrown if the user does not exist
	 */
	boolean isNPC(String name) throws UserDoesNotExistException;

	/**
	 * Used to create a new NPC.
	 *
	 * @param name the name to give the new NPC
	 * @return true if the NPC was successfully created, false if not (should never happen)
	 */
	boolean createNPC(String name);

	/**
	 * Used to remove a NPC with the given name
	 *
	 * @param name the name of the NPC to remove
	 * @throws UserDoesNotExistException thrown if the NPC does not exist
	 */
	void removeNPC(String name) throws UserDoesNotExistException;
}
