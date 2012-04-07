package com.earth2me.essentials.bukkit;

import com.earth2me.essentials.api.server.IWorld;
import com.earth2me.essentials.api.server.Location;
import lombok.Delegate;
import lombok.Getter;


public class BukkitLocation extends Location
{
	public static class BukkitLocationFactory implements LocationFactory
	{
		private final org.bukkit.Server server;

		public BukkitLocationFactory(org.bukkit.Server server)
		{
			this.server = server;
		}

		@Override
		public Location create(String worldName, double x, double y, double z, double yaw, double pitch)
		{
			org.bukkit.World world = server.getWorld(worldName);
			return new BukkitLocation(new org.bukkit.Location(world, x, y, z, (float)yaw, (float)pitch));
		}

		@Override
		public Location create(IWorld world, double x, double y, double z, double yaw, double pitch)
		{
			return new BukkitLocation(new org.bukkit.Location(((World)world).getBukkitWorld(), x, y, z, (float)yaw, (float)pitch));
		}
	}


	private interface Excludes
	{
		org.bukkit.World getWorld();
	}
	@Delegate(excludes =
	{
		Excludes.class
	})
	@Getter
	private final org.bukkit.Location bukkitLocation;

	public BukkitLocation(org.bukkit.Location bukkitLocation)
	{
		this.bukkitLocation = bukkitLocation;
	}

	@Override
	public IWorld getWorld()
	{
		return new World(bukkitLocation.getWorld());
	}
	
	@Override
	public double distanceSquared(final Location location)
	{
		return bukkitLocation.distanceSquared(((BukkitLocation)location).getBukkitLocation());
	}
	
}
