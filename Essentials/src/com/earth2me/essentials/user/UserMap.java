package com.earth2me.essentials.user;

import com.earth2me.essentials.api.IEssentials;
import com.earth2me.essentials.api.IUser;
import com.earth2me.essentials.api.IUserMap;
import com.earth2me.essentials.api.InvalidNameException;
import com.earth2me.essentials.api.server.Player;
import com.earth2me.essentials.storage.StorageObjectMap;
import com.earth2me.essentials.utils.Util;
import java.io.File;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import org.bukkit.Bukkit;


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

	@Override
	public IUser matchUser(final String name, final boolean includeHidden, final boolean includeOffline) throws TooManyMatchesException
	{
		final Set<IUser> users = matchUsers(name, includeHidden, includeOffline);
		if (users == null || users.isEmpty())
		{
			return null;
		}
		else
		{
			if (users.size() > 1)
			{
				throw new TooManyMatchesException();
			}
			else
			{
				return users.iterator().next();
			}
		}
	}

	@Override
	public Set<IUser> matchUsers(final String name, final boolean includeHidden, final boolean includeOffline)
	{
		final String colorlessName = Util.stripColor(name);
		final String[] search = colorlessName.split(",");
		final boolean multisearch = search.length > 1;
		final Set<IUser> result = new LinkedHashSet<IUser>();
		final String nicknamePrefix = Util.stripColor(getNickNamePrefix());
		for (String searchString : search)
		{
			if (searchString.isEmpty())
			{
				continue;
			}

			if (searchString.startsWith(nicknamePrefix))
			{
				searchString = searchString.substring(nicknamePrefix.length());
			}
			searchString = searchString.toLowerCase(Locale.ENGLISH);
			final boolean multimatching = searchString.endsWith("*");
			if (multimatching)
			{
				searchString = searchString.substring(0, searchString.length() - 1);
			}
			Player match = null;
			for (Player player : ess.getServer().getOnlinePlayers())
			{
				if (player.getName().equalsIgnoreCase(searchString)
					&& (includeHidden || (includeOffline && player.getUser().isHidden())))
				{
					match = player;
					break;
				}
			}
			if (match != null)
			{
				if (multimatching || multisearch)
				{
					result.add(match.getUser());
				}
				else
				{
					return Collections.singleton(match.getUser());
				}
			}
			for (Player player : ess.getServer().getOnlinePlayers())
			{
				final String nickname = player.getUser().getData().getNickname();
				if (nickname != null && !nickname.isEmpty()
					&& nickname.equalsIgnoreCase(searchString)
					&& (includeHidden || (includeOffline && player.getUser().isHidden())))
				{
					if (multimatching || multisearch)
					{
						result.add(player.getUser());
					}
					else
					{
						return Collections.singleton(player.getUser());
					}
				}
			}
			if (includeOffline)
			{
				for (String playerName : getAllUniqueUsers())
				{
					if (playerName.equals(searchString))
					{
						match = getUser(playerName);
						break;
					}
				}
				if (match != null)
				{
					if (multimatching || multisearch)
					{
						result.add(match.getUser());
					}
					else
					{
						return Collections.singleton(match.getUser());
					}
				}
			}
			if (multimatching || match == null)
			{
				for (Player player : ess.getServer().getOnlinePlayers())
				{
					if (player.getName().toLowerCase(Locale.ENGLISH).startsWith(searchString)
						&& (includeHidden || (includeOffline && player.getUser().isHidden())))
					{
						result.add(player.getUser());
						break;
					}
					final String nickname = player.getUser().getData().getNickname();
					if (nickname != null && !nickname.isEmpty()
						&& nickname.toLowerCase(Locale.ENGLISH).startsWith(searchString)
						&& (includeHidden || (includeOffline && player.getUser().isHidden())))
					{
						result.add(player.getUser());
						break;
					}
				}
				if (includeOffline)
				{
					for (String playerName : getAllUniqueUsers())
					{
						if (playerName.startsWith(searchString))
						{
							result.add(getUser(playerName));
							break;
						}
					}
				}
			}
		}
		return result;
	}

	private String getNickNamePrefix()
	{
		ess.getSettings().acquireReadLock();
		try
		{
			return ess.getSettings().getData().getChat().getNicknamePrefix();
		}
		finally
		{
			ess.getSettings().unlock();
		}
	}
}
