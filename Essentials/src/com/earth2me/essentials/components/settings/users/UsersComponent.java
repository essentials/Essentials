package com.earth2me.essentials.components.settings.users;

import com.earth2me.essentials.api.IContext;
import com.earth2me.essentials.api.IEssentials;
import com.earth2me.essentials.storage.StorageComponent;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.bukkit.entity.Player;


public class UsersComponent extends StorageComponent<Users, IEssentials> implements IUsersComponent
{
	private transient final Map<String, IUserComponent> cache = new HashMap<String, IUserComponent>();

	public UsersComponent(final IContext context, final IEssentials plugin)
	{
		super(context, Users.class, plugin);
	}

	@Override
	public String getContainerId()
	{
		return "users";
	}

	@Override
	public boolean userExists(final String name)
	{
		if (getContext().getServer().getPlayer(name) != null)
		{
			return true;
		}
		else
		{
			return isUserPersistent(name);
		}
	}

	private boolean isUserPersistent(final String name)
	{
		acquireReadLock();
		try
		{
			return getData().getUsers().containsKey(name.toLowerCase(Locale.ENGLISH).intern());
		}
		finally
		{
			unlock();
		}
	}

	@Override
	public IUserComponent getUser(final String name)
	{
		final String lowerName = name.toLowerCase(Locale.ENGLISH).intern();

		if (cache.containsKey(lowerName))
		{
			return cache.get(lowerName);
		}
		else
		{
			IUserComponent user = instantiate(name);
			if (user == null)
			{
				return null;
			}

			getContext().getEssentials().add(user);

			return cache.put(lowerName, user);
		}
	}

	private IUserComponent instantiate(final String name)
	{
		final String lowerName = name.toLowerCase(Locale.ENGLISH).intern();

		for (Player player : getContext().getServer().getOnlinePlayers())
		{
			// This is actually a safe comparision--strings are internalized.
			// Do NOT use String.equals.  You will undermine the efficiency of inernalization.
			if (player.getName().toLowerCase(Locale.ENGLISH).intern() == lowerName)
			{
				return new UserComponent(player, getContext());
			}
		}

		if (!isUserPersistent(lowerName))
		{
			return null;
		}

		return new UserComponent(new OfflineUser(lowerName, getContext()), getContext());
	}

	@Override
	public boolean removeUser(final String name)
	{
		final String lowerName = name.toLowerCase(Locale.ENGLISH).intern();
		if (!cache.containsKey(lowerName))
		{
			return false;
		}

		getContext().getEssentials().remove(cache.remove(lowerName));
		return true;
	}

	@Override
	public Set<String> getAllUniqueUsers()
	{
		return cache.keySet();
	}

	@Override
	public int getUniqueUsers()
	{
		return cache.size();
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
			user = new UserComponent(player, getContext());
		}
		else
		{
			user.setBase(new StatelessPlayer(player));
		}

		return user;
	}

	@Override
	public void close()
	{
		for (Entry<String, IUserComponent> entry : cache.entrySet())
		{
			getContext().getEssentials().remove(entry.getValue());
		}

		cache.clear();
	}
}
