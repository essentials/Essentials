package net.ess3.api;

import java.util.Collection;
import org.bukkit.Location;


public interface IJails extends IReload
{
	/**
	 * Used to get the location of a jail with the given name
	 *
	 * @param jailName the name of the jail to get
	 * @return the location of the jail
	 * @throws Exception if the jail does not exist
	 */
	Location getJail(String jailName) throws Exception;

	/**
	 * Used to get a list of jails by name
	 *
	 * @return a list of jails
	 * @throws Exception
	 */
	Collection<String> getList() throws Exception;

	/**
	 * Used to get the number of jails set
	 *
	 * @return the number of jails
	 */
	int getCount();

	/**
	 * Used to remove a jail
	 *
	 * @param jail the name of the jail to remove
	 * @throws Exception
	 */
	void removeJail(String jail) throws Exception;

	/**
	 * Used to send a user to jail
	 *
	 * @param user the user to send to jail
	 * @param jail the name of the jail to send them to
	 * @throws Exception
	 */
	void sendToJail(IUser user, String jail) throws Exception;

	/**
	 * Used to set a jail
	 *
	 * @param jailName the name for the jail
	 * @param loc the location to set the jail at
	 * @throws Exception
	 */
	void setJail(String jailName, Location loc) throws Exception;
}
