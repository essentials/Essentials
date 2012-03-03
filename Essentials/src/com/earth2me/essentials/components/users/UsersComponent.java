package com.earth2me.essentials.components.users;

import com.earth2me.essentials.api.IContext;
import com.earth2me.essentials.api.InvalidNameException;
import com.earth2me.essentials.storage.StorageComponentMap;
import java.io.File;
import java.util.Locale;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;


public class UsersComponent extends StorageComponentMap<IUserComponent> implements IUsersComponent {
	public UsersComponent(final IContext ess)
	{
		super(ess, "users");
	}

	@Override
	public boolean userExists(final String name)
	{
		return objectExists(name);
	}

	@Override
	public IUserComponent getUser(final String name)
	{
		return getObject(name);
	}

	@Override
	public IUserComponent load(final String name) throws Exception
	{
		for (Player player : context.getServer().getOnlinePlayers())
		{
			if (player.getName().equalsIgnoreCase(name))
			{
				keys.add(name.toLowerCase(Locale.ENGLISH));
				return new UserComponent(player, context);
			}
		}
		final File userFile = getUserFile(name);
		if (userFile.exists())
		{
			keys.add(name.toLowerCase(Locale.ENGLISH));
			return new UserComponent(Bukkit.getOfflinePlayer(name), context);
		}
		throw new Exception("User not found!");
	}

	@Override
	public boolean removeUser(final String name) throws InvalidNameException
	{
		return removeObject(name);
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

	public File getUserFile(final String name) throws InvalidNameException
	{
		return getStorageFile(name);
	}

	@Override
	public IUserComponent getUser(final Player player)
	{
		if (player instanceof IUserComponent)
		{
			return (IUserComponent)player;
		}
		IUserComponent user = getUser(player.getName());

		if (user == null)
		{
			user = new UserComponent(player, context);
		}
		else
		{
			((UserComponent)user).getStatelessPlayer().setOnlinePlayer(player);
		}
		return user;
	}
}