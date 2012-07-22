package net.ess3.api.server;

import java.util.UUID;
import org.bukkit.TreeType;

public interface World {
	String getName();

	boolean generateTree(Location safeLocation, TreeType tree);

	int getHighestBlockYAt(int topX, int topZ);

	ItemStack dropItem(Location loc, ItemStack stack);

	UUID getUID();

	Location getSpawnLocation();

	void dropItemNaturally(Location location, ItemStack overflowStack);

	void setStorm(boolean b);

	void setWeatherDuration(int i);
	
	long getTime();

	boolean setSpawnLocation(int blockX, int blockY, int blockZ);	
}
