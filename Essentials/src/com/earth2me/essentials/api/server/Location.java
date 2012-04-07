package com.earth2me.essentials.api.server;


public abstract class Location
{
	public interface LocationFactory {
		Location create(String worldName, double x, double y, double z, double yaw, double pitch);
		Location create(IWorld world, double x, double y, double z, double yaw, double pitch);
	}
	private static LocationFactory factory;
	
	public static void setFactory(final LocationFactory factory) {
		Location.factory = factory;
	}
	
	public static Location create(String worldName, double x, double y, double z) {
		return factory.create(worldName, x, y, z, 0, 0);
	}
	
	public static Location create(String worldName, double x, double y, double z, double yaw, double pitch) {
		return factory.create(worldName, x, y, z, yaw, pitch);
	}
	
	public static Location create(IWorld world, double x, double y, double z) {
		return factory.create(world, x, y, z, 0, 0);
	}
	
	public static Location create(IWorld world, double x, double y, double z, double yaw, double pitch) {
		return factory.create(world, x, y, z, yaw, pitch);
	}
	
	
	public abstract double getX();

	public abstract double getY();

	public abstract double getZ();

	public abstract float getYaw();

	public abstract float getPitch();

	public abstract int getBlockX();

	public abstract int getBlockY();

	public abstract int getBlockZ();
	
	public abstract IWorld getWorld();
	
	public abstract double distanceSquared(Location location);
}
