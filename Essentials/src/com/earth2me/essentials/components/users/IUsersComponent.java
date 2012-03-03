package com.earth2me.essentials.components.users;

import com.earth2me.essentials.api.InvalidNameException;
import com.earth2me.essentials.storage.IStorageComponentMap;
import java.util.Set;
import org.bukkit.entity.Player;


public interface IUsersComponent extends IStorageComponentMap<IUserComponent>
{
	boolean userExists(final String name);

	IUserComponent getUser(final Player player);

	IUserComponent getUser(final String playerName);

	boolean removeUser(final String name) throws InvalidNameException;

	Set<String> getAllUniqueUsers();

	int getUniqueUsers();
}
