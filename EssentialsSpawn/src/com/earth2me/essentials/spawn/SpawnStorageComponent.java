package com.earth2me.essentials.spawn;

import com.earth2me.essentials.api.IContext;
import com.earth2me.essentials.components.settings.Spawns;
import com.earth2me.essentials.components.users.IUserComponent;
import com.earth2me.essentials.storage.IStorageComponent;
import com.earth2me.essentials.storage.LocationData.WorldNotLoadedException;
import com.earth2me.essentials.storage.StorageComponent;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.EventPriority;


public final class SpawnStorageComponent extends StorageComponent<Spawns> implements IStorageComponent<Spawns>
{
	public SpawnStorageComponent(final IContext context)
	{
		super(context, Spawns.class);
	}

	@Override
	public void initialize()
	{
		super.initialize();

		reload();
	}

	public void setSpawn(final Location loc, final String group)
	{
		acquireWriteLock();
		try
		{
			if (getData().getSpawns() == null)
			{
				getData().setSpawns(new HashMap<String, com.earth2me.essentials.storage.LocationData>());
			}
			getData().getSpawns().put(group.toLowerCase(Locale.ENGLISH), new com.earth2me.essentials.storage.LocationData(loc));
		}
		finally
		{
			unlock();
		}

		if ("default".equalsIgnoreCase(group))
		{
			loc.getWorld().setSpawnLocation(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
		}
	}

	public Location getSpawn(final String group)
	{
		acquireReadLock();
		try
		{
			if (getData().getSpawns() == null || group == null)
			{
				return getWorldSpawn();
			}
			final Map<String, com.earth2me.essentials.storage.LocationData> spawnMap = getData().getSpawns();
			String groupName = group.toLowerCase(Locale.ENGLISH);
			if (!spawnMap.containsKey(groupName))
			{
				groupName = "default";
			}
			if (!spawnMap.containsKey(groupName))
			{
				return getWorldSpawn();
			}
			try
			{
				return spawnMap.get(groupName).getBukkitLocation();
			}
			catch (WorldNotLoadedException ex)
			{
				return getWorldSpawn();
			}
		}
		finally
		{
			unlock();
		}
	}

	private Location getWorldSpawn()
	{
		for (World world : getContext().getServer().getWorlds())
		{
			if (world.getEnvironment() != World.Environment.NORMAL)
			{
				continue;
			}
			return world.getSpawnLocation();
		}
		return getContext().getServer().getWorlds().get(0).getSpawnLocation();
	}

	public EventPriority getRespawnPriority()
	{
		acquireReadLock();
		try
		{
			for (EventPriority priority : EventPriority.values())
			{
				if (priority.toString().equalsIgnoreCase(getData().getRespawnPriority()))
				{
					return priority;
				}
			}
			return EventPriority.NORMAL;
		}
		finally
		{
			unlock();
		}
	}

	public Location getNewbieSpawn()
	{
		acquireReadLock();
		try
		{
			if (getData().getNewbieSpawn() == null || getData().getNewbieSpawn().isEmpty()
				|| getData().getNewbieSpawn().equalsIgnoreCase("none"))
			{
				return null;
			}
			return getSpawn(getData().getNewbieSpawn());
		}
		finally
		{
			unlock();
		}
	}

	public boolean getAnnounceNewPlayers()
	{
		acquireReadLock();
		try
		{
			return getData().getNewPlayerAnnouncement() != null && !getData().getNewPlayerAnnouncement().isEmpty();
		}
		finally
		{
			unlock();
		}
	}

	public String getAnnounceNewPlayerFormat(IUserComponent user)
	{
		acquireReadLock();
		try
		{
			return getData().getNewPlayerAnnouncement().replace('&', '§').replace("§§", "&").replace("{PLAYER}", user.getDisplayName()).replace("{DISPLAYNAME}", user.getDisplayName()).replace("{GROUP}", getContext().getGroups().getMainGroup(user)).replace("{USERNAME}", user.getName()).replace("{ADDRESS}", user.getAddress().toString());
		}
		finally
		{
			unlock();
		}
	}

	@Override
	public String getContainerId()
	{
		return "spawns-database";
	}
}
