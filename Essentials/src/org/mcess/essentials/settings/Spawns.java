package org.mcess.essentials.settings;

import org.mcess.essentials.storage.MapValueType;
import org.mcess.essentials.storage.StorageObject;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.Location;

public class Spawns implements StorageObject
{
	@MapValueType(Location.class)
	private Map<String, Location> spawns = new HashMap<String, Location>();

	public Map<String,Location> getSpawns()
	{
		return spawns;
	}

	public void setSpawns(Map<String, Location> spawns)
	{
		this.spawns =  spawns;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Spawns spawns1 = (Spawns) o;
		return !(spawns != null ? !spawns.equals(spawns1.spawns) : spawns1.spawns != null);
	}

	@Override
	public int hashCode() {
		return spawns != null ? spawns.hashCode() : 0;
	}
}
