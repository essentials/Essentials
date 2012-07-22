package net.ess3.storage;

import java.lang.ref.WeakReference;
import net.ess3.api.server.Location;
import net.ess3.api.server.World;


public class StoredLocation
{
	private WeakReference<Location> location;
	private final String worldname;
	private final double x;
	private final double y;
	private final double z;
	private final float yaw;
	private final float pitch;

	public StoredLocation(Location loc)
	{
		location = new WeakReference<Location>(loc);
		worldname = loc.getWorld().getName();
		x = loc.getX();
		y = loc.getY();
		z = loc.getZ();
		yaw = loc.getYaw();
		pitch = loc.getPitch();
	}

	public StoredLocation(String worldname, double x, double y, double z, float yaw, float pitch)
	{
		this.worldname = worldname;
		this.x = x;
		this.y = y;
		this.z = z;
		this.yaw = yaw;
		this.pitch = pitch;
	}

	public StoredLocation(String worldname, double x, double y, double z)
	{
		this.worldname = worldname;
		this.x = x;
		this.y = y;
		this.z = z;
		this.yaw = 0f;
		this.pitch = 0f;
	}

	public Location getStoredLocation() throws WorldNotLoadedException
	{

		Location loc = location == null ? null : location.get();
		if (loc == null)
		{
			loc = Location.create(worldname, getX(), getY(), getZ(), getYaw(), getPitch());
			location = new WeakReference<Location>(loc);
		}
		return loc;
	}

	public String getWorldName()
	{
		return worldname;
	}

	public double getX()
	{
		return x;
	}

	public double getY()
	{
		return y;
	}

	public double getZ()
	{
		return z;
	}

	public float getYaw()
	{
		return yaw;
	}

	public float getPitch()
	{
		return pitch;
	}


	public static class WorldNotLoadedException extends Exception
	{
		public WorldNotLoadedException(String worldname)
		{
			super("World " + worldname + " is not loaded.");
		}
	}
}
