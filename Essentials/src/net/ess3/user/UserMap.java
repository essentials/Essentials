package net.ess3.user;

import static net.ess3.I18n._;
import java.io.File;
import java.util.*;
import java.util.regex.Pattern;
import org.bukkit.entity.Player;
import net.ess3.api.IEssentials;
import net.ess3.api.IUser;
import net.ess3.api.IUserMap;
import net.ess3.api.InvalidNameException;
import net.ess3.storage.StorageObjectMap;
import net.ess3.utils.FormatUtil;


public class UserMap extends StorageObjectMap<IUser> implements IUserMap
{
	private final Map<String, Player> prejoinedPlayers = new HashMap<String, Player>();

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
		final String lowercaseName = name.toLowerCase(Locale.ENGLISH);
		if (!lowercaseName.equals(name))
		{
			final IUser user = getUser(lowercaseName);
			if (user == null)
			{
				throw new Exception(_("userNotFound"));
			}
			else
			{
				return user;
			}
		}
		Player player = prejoinedPlayers.get(name);
		if (player == null)
		{
			player = ess.getServer().getPlayerExact(name);
		}
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
		throw new Exception(_("userNotFound"));
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
		return getObject(player.getName());
	}

	@Override
	public IUser matchUser(final String name, final boolean includeOffline) throws TooManyMatchesException, PlayerNotFoundException
	{
		return matchUser(name, true, includeOffline, null);
	}

	@Override
	public IUser matchUserExcludingHidden(final String name, final Player requester) throws TooManyMatchesException, PlayerNotFoundException
	{
		return matchUser(name, false, false, requester);
	}

	public IUser matchUser(final String name, final boolean includeHidden, final boolean includeOffline, final Player requester) throws TooManyMatchesException, PlayerNotFoundException
	{
		final Set<IUser> users = matchUsers(name, includeHidden, includeOffline, requester);
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
	public Set<IUser> matchUsers(final String name, final boolean includeOffline)
	{
		return matchUsers(name, true, includeOffline, null);
	}

	@Override
	public Set<IUser> matchUsersExcludingHidden(final String name, final Player requester)
	{
		return matchUsers(name, false, false, requester);
	}

	private final Pattern comma = Pattern.compile(",");

	public Set<IUser> matchUsers(final String name, final boolean includeHidden, final boolean includeOffline, final Player requester)
	{
		final String colorlessName = FormatUtil.stripColor(name);
		final String[] search = comma.split(colorlessName);
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
				if (player.getName().equalsIgnoreCase(searchString) && (includeHidden || includeOffline || requester == null || requester.canSee(player)))
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
				if (nickname != null && !nickname.isEmpty() && nickname.equalsIgnoreCase(
						searchString) && (includeHidden || includeOffline || requester == null || requester.canSee(player)))
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
					if (player.getName().toLowerCase(Locale.ENGLISH).startsWith(
							searchString) && (includeHidden || includeOffline || requester == null || requester.canSee(player)))
					{
						result.add(getUser(player));
						break;
					}
					final String nickname = getUser(player).getData().getNickname();
					if (nickname != null && !nickname.isEmpty() && nickname.toLowerCase(Locale.ENGLISH).startsWith(
							searchString) && (includeHidden || includeOffline || requester == null || requester.canSee(player)))
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

	@Override
	public void addPrejoinedPlayer(Player player)
	{
		prejoinedPlayers.put(player.getName().toLowerCase(Locale.ENGLISH), player);
	}

	@Override
	public void removePrejoinedPlayer(Player player)
	{
		prejoinedPlayers.remove(player.getName().toLowerCase(Locale.ENGLISH));
	}
}
