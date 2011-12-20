package com.earth2me.essentials.spawn;

import com.earth2me.essentials.IEssentials;
import com.earth2me.essentials.IEssentialsModule;
import com.earth2me.essentials.settings.Spawns;
import com.earth2me.essentials.storage.AsyncStorageObjectHolder;
import java.io.File;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.bukkit.Location;
import org.bukkit.World;


public class SpawnStorage extends AsyncStorageObjectHolder<Spawns> implements IEssentialsModule
{
	public SpawnStorage(final IEssentials ess)
	{
		super(ess, Spawns.class);
		reloadConfig();
	}

	@Override
	public File getStorageFile()
	{
		return new File(ess.getDataFolder(), "spawn.yml");
	}

	public void setSpawn(final Location loc, final String group)
	{
		acquireWriteLock();
		try
		{
			if (getData().getSpawns() == null)
			{
				getData().setSpawns(new HashMap<String, Location>());
			}
			getData().getSpawns().put(group.toLowerCase(Locale.ENGLISH), loc);
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
			final Map<String, Location> spawnMap = getData().getSpawns();
			String groupName = group.toLowerCase(Locale.ENGLISH);
			if (!spawnMap.containsKey(groupName))
			{
				groupName = "default";
			}
			if (!spawnMap.containsKey(groupName))
			{
				return getWorldSpawn();
			}
			return spawnMap.get(groupName);
		}
		finally
		{
			unlock();
		}
	}

	private Location getWorldSpawn()
	{
		for (World world : ess.getServer().getWorlds())
		{
			if (world.getEnvironment() != World.Environment.NORMAL)
			{
				continue;
			}
			return world.getSpawnLocation();
		}
		return ess.getServer().getWorlds().get(0).getSpawnLocation();
	}
}
