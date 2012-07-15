package com.earth2me.essentials.api;

import com.earth2me.essentials.api.server.Player;
import com.earth2me.essentials.user.TooManyMatchesException;
import java.io.File;
import java.util.List;
import java.util.Set;


public interface IUserMap extends IReload
{
	boolean userExists(final String name);

	IUser getUser(final Player player);

	IUser getUser(final String playerName);

	void removeUser(final String name) throws InvalidNameException;

	Set<String> getAllUniqueUsers();

	int getUniqueUsers();

	File getUserFile(final String name) throws InvalidNameException;
	
	IUser matchUser(final String name, final boolean includeHidden, final boolean onlineOnly) throws TooManyMatchesException;
	
	Set<IUser> matchUsers(final String name, final boolean includeHidden, final boolean onlineOnly);
}
