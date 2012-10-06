package net.ess3.user;

import java.io.File;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;
import net.ess3.api.IEssentials;
import net.ess3.api.IUser;
import net.ess3.api.IUserMap;
import net.ess3.api.InvalidNameException;
import net.ess3.storage.StorageObjectMap;
import net.ess3.utils.FormatUtil;
import net.ess3.utils.Util;
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
		String lowercaseName = name.toLowerCase(Locale.ENGLISH);
		if (!lowercaseName.equals(name))
		{
			IUser user = getUser(lowercaseName);
			if (user == null)
			{
				throw new Exception("User not found!");
			}
			else
			{
				return user;
			}
		}
		Player player = ess.getServer().getPlayerExact(name);
		if (player != null)
		{
			return new User(ess.getServer().getOfflinePlayer(player.getName()), ess);
		}
		final File userFile = getUserFile(name);
		if (userFile.exists())
		{
			keys.add(name.toLowerCase(Locale.ENGLISH));
			return new User(ess.getServer().getOfflinePlayer(name), ess);
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
		IUser user = getObject(player.getName());
		if (user == null)
		{
			user = new User(player, ess);
		}
		return user;
	}

	@Override
	public IUser matchUser(final String name, final boolean includeHidden, final boolean includeOffline) throws TooManyMatchesException, PlayerNotFoundException
	{
		final Set<IUser> users = matchUsers(name, includeHidden, includeOffline);
		if (users.isEmpty())
		{
			throw new PlayerNotFoundException();
		}
		else
		{
			if (users.size() > 1)
			{
				throw new TooManyMatchesException(users);
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
		final String colorlessName = FormatUtil.stripColor(name);
		final String[] search = colorlessName.split(",");
		final boolean multisearch = search.length > 1;
		final Set<IUser> result = new LinkedHashSet<IUser>();
		final String nicknamePrefix = FormatUtil.stripColor(getNickNamePrefix());
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
					&& (includeHidden || (includeOffline && getUser(player).isHidden())))
				{
					match = player;
					break;
				}
			}
			if (match != null)
			{
				if (multimatching || multisearch)
				{
					result.add(getUser(match));
				}
				else
				{
					return Collections.singleton(getUser(match));
				}
			}
			for (Player player : ess.getServer().getOnlinePlayers())
			{
				final String nickname = getUser(player).getData().getNickname();
				if (nickname != null && !nickname.isEmpty()
					&& nickname.equalsIgnoreCase(searchString)
					&& (includeHidden || (includeOffline && getUser(player).isHidden())))
				{
					if (multimatching || multisearch)
					{
						result.add(getUser(player));
					}
					else
					{
						return Collections.singleton(getUser(player));
					}
				}
			}
			if (includeOffline)
			{
				IUser matchu = null;
				for (String playerName : getAllUniqueUsers())
				{
					if (playerName.equals(searchString))
					{
						matchu = getUser(playerName);
						break;
					}
				}
				if (matchu != null)
				{
					if (multimatching || multisearch)
					{
						result.add(matchu);
					}
					else
					{
						return Collections.singleton(matchu);
					}
				}
			}
			if (multimatching || match == null)
			{
				for (Player player : ess.getServer().getOnlinePlayers())
				{
					if (player.getName().toLowerCase(Locale.ENGLISH).startsWith(searchString)
						&& (includeHidden || (includeOffline && getUser(player).isHidden())))
					{
						result.add(getUser(player));
						break;
					}
					final String nickname = getUser(player).getData().getNickname();
					if (nickname != null && !nickname.isEmpty()
						&& nickname.toLowerCase(Locale.ENGLISH).startsWith(searchString)
						&& (includeHidden || (includeOffline && getUser(player).isHidden())))
					{
						result.add(getUser(player));
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
		return ess.getSettings().getData().getChat().getNicknamePrefix();
	}
}
