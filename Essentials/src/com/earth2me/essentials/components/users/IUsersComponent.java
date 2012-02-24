package com.earth2me.essentials.components.users;

import com.earth2me.essentials.api.InvalidNameException;
import com.earth2me.essentials.components.IComponent;
import java.io.File;
import java.util.Set;
import org.bukkit.entity.Player;


public interface IUsersComponent extends IComponent
{
	boolean userExists(final String name);

	IUser getUser(final Player player);

	IUser getUser(final String playerName);

	void removeUser(final String name) throws InvalidNameException;

	Set<String> getAllUniqueUsers();

	int getUniqueUsers();

	File getUserFile(final String name) throws InvalidNameException;
}
