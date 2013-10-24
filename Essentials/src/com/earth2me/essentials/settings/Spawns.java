package com.earth2me.essentials.settings;

import com.earth2me.essentials.storage.MapValueType;
import com.earth2me.essentials.storage.StorageObject;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.Location;


public class Spawns implements StorageObject
{
	@MapValueType(Location.class)
	private Map<String, Location> spawns = new HashMap<String, Location>();

	public Map<String, Location> getSpawns()
	{
		return spawns;
	}

	public void setSpawns(Map<String, Location> spawns)
	{
		this.spawns = spawns;
	}

	public boolean canEqual(Object other)
	{
		return other instanceof Spawns;
	}
}
