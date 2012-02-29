package com.earth2me.essentials.components.users;

import com.earth2me.essentials.api.IEssentials;
import com.earth2me.essentials.storage.IMultiStorageComponent;
import java.util.Set;
import org.bukkit.entity.Player;


public interface IUsersComponent extends IMultiStorageComponent<UserSurrogate, IEssentials>
{
	boolean userExists(final String name);

	IUserComponent getUser(final Player player);

	IUserComponent getUser(final String playerName);

	boolean removeUser(final String name);

	Set<String> getAllUniqueUsers();

	int getUniqueUsers();
}
