package net.ess3.api;

import java.io.File;
import java.util.Set;
import net.ess3.user.PlayerNotFoundException;
import net.ess3.user.TooManyMatchesException;
import org.bukkit.entity.Player;


public interface IUserMap extends IReload
{
	/**
	 * Used to check if a user exists (they exist if they have userdata)
	 *
	 * @param name the name of the user to check
	 * @return true if the user exists, false if not
	 */
	boolean userExists(final String name);

	/**
	 * Used to get the user associated with the given Player object
	 *
	 * @param player the Player object
	 * @return the User object
	 */
	IUser getUser(final Player player);

	/**
	 * Used to get the user associated with the given name
	 *
	 * @param playerName the name of the player
	 * @return the User object
	 */
	IUser getUser(final String playerName);

	/**
	 * Used to remove a user from the userMap
	 *
	 * @param name the name of the user to remove
	 * @throws InvalidNameException if the name does not match any user in the UserMap
	 */
	void removeUser(final String name) throws InvalidNameException;

	/**
	 * Gets a set of all the unique users in the UserMap
	 *
	 * @return the Users
	 */
	Set<String> getAllUniqueUsers();

	/**
	 * Gets the number of unique users in the UserMap
	 *
	 * @return the number of unique users
	 */
	int getUniqueUsers();

	/**
	 * Used to get the file of the given user
	 *
	 * @param name the name of the user
	 * @return the file
	 * @throws InvalidNameException
	 */
	File getUserFile(final String name) throws InvalidNameException;

	/**
	 * Used to match a user to a given name
	 * This method never returns null and includes hidden players.
	 *
	 * @param name the name to match
	 * @param includeOffline set to true to check offline users as well, false if not
	 * @return the matched user
	 * @throws TooManyMatchesException if more than one player is found matching the name
	 * @throws PlayerNotFoundException if the player matching the name is not found
	 */
	IUser matchUser(final String name, final boolean includeOffline) throws TooManyMatchesException, PlayerNotFoundException;

	/**
	 * Used to match a user to a given name
	 * This method never returns null and is for online players only.
	 *
	 * @param name the name to match
	 * @param requester Can be null, if the requester is the console
	 * @return the matched user
	 * @throws TooManyMatchesException if more than one player is found matching the name
	 * @throws PlayerNotFoundException if the player matching the name is not found
	 */
	IUser matchUserExcludingHidden(final String name, final Player requester) throws TooManyMatchesException, PlayerNotFoundException;

	/**
	 * Used to match multiple users
	 * This method never returns null and includes hidden players.
	 *
	 * @param name the name to match
	 * @param includeOffline set to true to check offline users as well, false if not
	 * @return the matched user(s)
	 */
	Set<IUser> matchUsers(final String name, final boolean includeOffline);

	/**
	 * Used to match multiple users
	 * This method never returns null and is for online players only.
	 *
	 * @param name the name to match
	 * @param requester Can be null, if the requester is the console
	 * @return the matched user(s)
	 */
	Set<IUser> matchUsersExcludingHidden(final String name, final Player requester);

	/**
	 * Used to add a player to the map of pre-joined players
	 * **TODO: we shouldn't be using player objects here**
	 *
	 * @param player the player to add
	 */
	void addPrejoinedPlayer(Player player);

	/**
	 * Used to remove a player from the map of pre-joined players
	 * **TODO: we shouldn't be using player objects here**
	 *
	 * @param player the player to remove
	 */
	void removePrejoinedPlayer(Player player);
}
