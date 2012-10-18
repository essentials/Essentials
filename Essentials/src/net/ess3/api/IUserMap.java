package net.ess3.api;

import java.io.File;
import java.util.Set;
import net.ess3.user.PlayerNotFoundException;
import net.ess3.user.TooManyMatchesException;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;


public interface IUserMap extends IReload
{
	boolean userExists(final String name);

	IUser getUser(final Player player);

	IUser getUser(final String playerName);

	void removeUser(final String name) throws InvalidNameException;

	Set<String> getAllUniqueUsers();

	int getUniqueUsers();

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

	Set<IUser> matchUsers(final String name, final boolean includeOffline);

	Set<IUser> matchUsersExcludingHidden(final String name, final Player requester);

	public void addPrejoinedPlayer(Player player);

	public void removePrejoinedPlayer(Player player);

	public Object getPlayer(LivingEntity livingEntity);
}
