package com.earth2me.essentials.anticheat.data;

import org.bukkit.Location;


/**
 * A class to store x,y,z triple data, instead of using bukkits Location objects, which can't be easily recycled
 *
 */
public final class PreciseLocation
{
	public double x;
	public double y;
	public double z;

	public PreciseLocation()
	{
		reset();
	}

	public final void set(Location location)
	{
		x = location.getX();
		y = location.getY();
		z = location.getZ();
	}

	public final void set(PreciseLocation location)
	{
		x = location.x;
		y = location.y;
		z = location.z;
	}

	public final boolean isSet()
	{
		return x != Double.MAX_VALUE;
	}

	public final void reset()
	{
		x = Double.MAX_VALUE;
		y = Double.MAX_VALUE;
		z = Double.MAX_VALUE;
	}

	public final boolean equals(Location location)
	{
		return location.getX() == x && location.getY() == y && location.getZ() == z;
	}
}
