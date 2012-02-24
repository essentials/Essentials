package com.earth2me.essentials.components.users;

import com.earth2me.essentials.api.IContext;
import com.earth2me.essentials.api.InvalidNameException;
import com.earth2me.essentials.storage.StorageObjectMap;
import java.io.File;
import java.util.Locale;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;


public class UsersComponent extends StorageObjectMap<IUser> implements IUsersComponent
{
	public UsersComponent(final IContext ess)
	{
		super(ess, "users");
	}

	@Override
	public String getTypeId()
	{
		return "UsersComponent";
	}

	@Override
	public void initialize()
	{
		// Intentionally blank.
	}

	@Override
	public void close()
	{
		// Intentionally blank.
	}

	@Override
	public boolean userExists(final String name)
	{
		return objectExists(name);
	}

	@Override
	public IUser getUser(final String name)
	{
		return getObject(name);
	}

	@Override
	public IUser load(final String name) throws Exception
	{
		for (Player player : ess.getServer().getOnlinePlayers())
		{
			if (player.getName().equalsIgnoreCase(name))
			{
				keys.add(name.toLowerCase(Locale.ENGLISH));
				return new User(player, ess);
			}
		}
		final File userFile = getUserFile(name);
		if (userFile.exists())
		{
			keys.add(name.toLowerCase(Locale.ENGLISH));
			return new User(Bukkit.getOfflinePlayer(name), ess);
		}
		throw new Exception("User not found!");
	}

	@Override
	public void removeUser(final String name) throws InvalidNameException
	{
		removeObject(name);
	}

	@Override
	public Set<String> getAllUniqueUsers()
	{
		return getAllKeys();
	}

	@Override
	public int getUniqueUsers()
	{
		return getKeySize();
	}

	@Override
	public File getUserFile(String name) throws InvalidNameException
	{
		return getStorageFile(name);
	}

	@Override
	public IUser getUser(final Player player)
	{
		if (player instanceof IUser)
		{
			return (IUser)player;
		}
		IUser user = getUser(player.getName());

		if (user == null)
		{
			user = new User(player, ess);
		}
		else
		{
			((User)user).update(player);
		}
		return user;
	}
}
