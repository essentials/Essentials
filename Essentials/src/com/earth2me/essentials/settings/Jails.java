package com.earth2me.essentials.settings;

import com.earth2me.essentials.storage.MapValueType;
import com.earth2me.essentials.storage.StorageObject;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.Location;


public class Jails implements StorageObject
{
	@MapValueType(Location.class)
	private Map<String, Location> jails = new HashMap<String, Location>();

	public Map<String,Location> getJails()
	{
		return jails;
	}

	public void setJails(Map<String, Location> jails)
	{
		this.jails =  jails;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Jails jails1 = (Jails) o;
		return !(jails != null ? !jails.equals(jails1.jails) : jails1.jails != null);
	}

	@Override
	public int hashCode() {
		return jails != null ? jails.hashCode() : 0;
	}
}
