package net.ess3.api;

import java.util.Collection;
import org.bukkit.Location;


public interface IJails extends IReload
{
	/**
	 *
	 * @param jailName
	 * @return
	 * @throws Exception
	 */
	Location getJail(String jailName) throws Exception;

	/**
	 *
	 * @return
	 * @throws Exception
	 */
	Collection<String> getList() throws Exception;

	/**
	 *
	 * @return
	 */
	int getCount();

	/**
	 *
	 * @param jail
	 * @throws Exception
	 */
	void removeJail(String jail) throws Exception;

	/**
	 *
	 * @param user
	 * @param jail
	 * @throws Exception
	 */
	void sendToJail(IUser user, String jail) throws Exception;

	/**
	 *
	 * @param jailName
	 * @param loc
	 * @throws Exception
	 */
	void setJail(String jailName, Location loc) throws Exception;
}
