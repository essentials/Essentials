package net.ess3.utils;

import org.bukkit.Location;
import org.bukkit.entity.Entity;


public class Target
{
	private final Location location;
	private final Entity entity;

	public Target(Location location)
	{
		this.location = location;
		this.entity = null;
	}

	public Target(Entity entity)
	{
		this.entity = entity;
		this.location = null;
	}

	public Location getLocation()
	{
		if (this.entity != null)
		{
			return this.entity.getLocation();
		}
		return location;
	}
}

