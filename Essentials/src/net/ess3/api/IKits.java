package net.ess3.api;

import java.util.Collection;
import net.ess3.commands.NoChargeException;
import net.ess3.settings.Kit;


public interface IKits extends IReload
{
	/**
	 * Used to get a kit by name
	 *
	 * @param kit the name of the kit to get
	 * @return the kit
	 * @throws Exception if the kit does not exist or is improperly defined (due to broken config formatting)
	 */
	Kit getKit(String kit) throws Exception;

	/**
	 * Sends a kit to a user
	 *
	 * @param user the user to send the kit to
	 * @param kit the kit to send
	 * @throws Exception if the user cannot afford the kit
	 */
	void sendKit(IUser user, String kit) throws Exception;

	/**
	 * Sends a kit to a user
	 *
	 * @param user the user to send the kit to
	 * @param kit the kit to send
	 * @throws Exception if the user cannot afford the kit
	 */
	void sendKit(IUser user, Kit kit) throws Exception;

	/**
	 * Used to get a list of kits by name
	 *
	 * @return the list of kits
	 * @throws Exception
	 */
	Collection<String> getList() throws Exception;

	/**
	 * Used to check if the list of kits is empty
	 *
	 * @return true if the list is empty, false if not
	 */
	boolean isEmpty();

	/**
	 * Used to check kit delay for cooldowns
	 *
	 * @param user the user to check
	 * @param kit the kit to check
	 * @throws NoChargeException if the kit cannot be used **TODO: use a better exception here**
	 */
	void checkTime(final IUser user, Kit kit) throws NoChargeException;
}
