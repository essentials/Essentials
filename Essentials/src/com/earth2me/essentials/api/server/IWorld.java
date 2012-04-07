package com.earth2me.essentials.api.server;

import java.util.UUID;
import org.bukkit.TreeType;

public interface IWorld {
	String getName();

	public boolean generateTree(Location safeLocation, TreeType tree);

	public int getHighestBlockYAt(int topX, int topZ);

	public ItemStack dropItem(Location loc, ItemStack stack);

	public UUID getUID();

	public Location getSpawnLocation();

	public void dropItemNaturally(Location location, ItemStack overflowStack);

	public void setStorm(boolean b);

	public void setWeatherDuration(int i);
}
