package net.ess3.api;

import net.ess3.user.TooManyMatchesException;
import java.io.File;
import java.util.Set;
import net.ess3.user.PlayerNotFoundException;
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
	
	IUser matchUser(final String name, final boolean includeHidden, final boolean includeOffline) throws TooManyMatchesException, PlayerNotFoundException;
	
	Set<IUser> matchUsers(final String name, final boolean includeHidden, final boolean includeOffline) throws PlayerNotFoundException;
}
