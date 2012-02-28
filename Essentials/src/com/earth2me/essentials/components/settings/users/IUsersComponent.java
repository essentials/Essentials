package com.earth2me.essentials.components.settings.users;

import com.earth2me.essentials.api.InvalidNameException;
import com.earth2me.essentials.components.IComponent;
import com.earth2me.essentials.storage.IStorageObjectHolder;
import java.io.File;
import java.util.Set;
import org.bukkit.entity.Player;


public interface IUsersComponent extends IComponent, IStorageObjectHolder<Users>
{
	boolean userExists(final String name);

	IUserComponent getUser(final Player player);

	IUserComponent getUser(final String playerName);

	boolean removeUser(final String name);

	Set<String> getAllUniqueUsers();

	int getUniqueUsers();
}
