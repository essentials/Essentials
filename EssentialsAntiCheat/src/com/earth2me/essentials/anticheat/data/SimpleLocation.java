package com.earth2me.essentials.anticheat.data;

import org.bukkit.Location;
import org.bukkit.block.Block;


/**
 * To avoid constantly creating and referencing "Location" objects, which in turn reference a whole lot of other
 * unnecessary stuff, rather use our own "Location" object which is easily reusable.
 *
 */
public final class SimpleLocation
{
	public int x;
	public int y;
	public int z;

	public SimpleLocation()
	{
		reset();
	}

	@Override
	public final boolean equals(Object object)
	{
		if (!(object instanceof SimpleLocation))
		{
			return false;
		}

		SimpleLocation simpleLocation = (SimpleLocation)object;

		if (!isSet() && !simpleLocation.isSet())
		{
			return true;
		}
		else if (!isSet() || !simpleLocation.isSet())
		{
			return false;
		}

		return simpleLocation.x == x && simpleLocation.y == y && simpleLocation.z == z;
	}

	@Override
	public final int hashCode()
	{
		return x * 1000000 + y * 1000 + z;
	}

	public final void set(Block block)
	{
		x = block.getX();
		y = block.getY();
		z = block.getZ();
	}

	public final void setLocation(Location location)
	{
		x = location.getBlockX();
		y = location.getBlockY();
		z = location.getBlockZ();
	}

	public final boolean isSet()
	{
		return x != Integer.MAX_VALUE;
	}

	public final void reset()
	{
		x = Integer.MAX_VALUE;
		y = Integer.MAX_VALUE;
		z = Integer.MAX_VALUE;
	}
}
