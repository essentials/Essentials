package net.ess3.api;

import java.io.File;
import java.util.Set;
import net.ess3.user.PlayerNotFoundException;
import net.ess3.user.TooManyMatchesException;
import org.bukkit.entity.Player;


public interface IUserMap extends IReload
{
	/**
	 *
	 * @param name
	 * @return
	 */
	boolean userExists(final String name);

	/**
	 *
	 * @param player
	 * @return
	 */
	IUser getUser(final Player player);

	/**
	 *
	 * @param playerName
	 * @return
	 */
	IUser getUser(final String playerName);

	/**
	 *
	 * @param name
	 * @throws InvalidNameException
	 */
	void removeUser(final String name) throws InvalidNameException;

	/**
	 *
	 * @return
	 */
	Set<String> getAllUniqueUsers();

	/**
	 *
	 * @return
	 */
	int getUniqueUsers();

	/**
	 *
	 * @param name
	 * @return
	 * @throws InvalidNameException
	 */
	File getUserFile(final String name) throws InvalidNameException;

	/**
	 * This method never returns null and includes hidden players.
	 *
	 * @param name
	 * @param includeOffline
	 * @return
	 * @throws TooManyMatchesException if more than one player is found matching the name
	 * @throws PlayerNotFoundException if the player matching the name is not found
	 */
	IUser matchUser(final String name, final boolean includeOffline) throws TooManyMatchesException, PlayerNotFoundException;

	/**
	 * This method never returns null and is for online players only.
	 *
	 * @param name
	 * @param requester Can be null, if the requester is the console
	 * @return
	 * @throws TooManyMatchesException if more than one player is found matching the name
	 * @throws PlayerNotFoundException if the player matching the name is not found
	 */
	IUser matchUserExcludingHidden(final String name, final Player requester) throws TooManyMatchesException, PlayerNotFoundException;

	/**
	 *
	 * @param name
	 * @param includeOffline
	 * @return
	 */
	Set<IUser> matchUsers(final String name, final boolean includeOffline);

	/**
	 *
	 * @param name
	 * @param requester
	 * @return
	 */
	Set<IUser> matchUsersExcludingHidden(final String name, final Player requester);

	/**
	 *
	 * @param player
	 */
	void addPrejoinedPlayer(Player player);

	/**
	 *
	 * @param player
	 */
	void removePrejoinedPlayer(Player player);
}
