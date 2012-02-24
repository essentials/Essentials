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
	public UsersComponent(final IContext context)
	{
		super(context, "users");
	}

	@Override
	public String getTypeId()
	{
		return "UsersComponent";
	}

	@Override
	public void initialize()
	{
	}

	@Override
	public void onEnable()
	{
		reload();
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
		for (Player player : context.getServer().getOnlinePlayers())
		{
			if (player.getName().equalsIgnoreCase(name))
			{
				keys.add(name.toLowerCase(Locale.ENGLISH));
				return new User(player, context);
			}
		}
		final File userFile = getUserFile(name);
		if (userFile.exists())
		{
			keys.add(name.toLowerCase(Locale.ENGLISH));
			return new User(Bukkit.getOfflinePlayer(name), context);
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
			user = new User(player, context);
		}
		else
		{
			((User)user).update(player);
		}
		return user;
	}

	@Override
	public void close()
	{
		cache.cleanUp();
	}
}
