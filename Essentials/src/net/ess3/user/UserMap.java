package net.ess3.user;

import java.io.File;
import java.util.Locale;
import java.util.Set;
import net.ess3.api.IEssentials;
import net.ess3.api.IUser;
import net.ess3.api.IUserMap;
import net.ess3.api.InvalidNameException;
import net.ess3.storage.StorageObjectMap;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;


public class UserMap extends StorageObjectMap<IUser> implements IUserMap
{
	public UserMap(final IEssentials ess)
	{
		super(ess, "users");
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
