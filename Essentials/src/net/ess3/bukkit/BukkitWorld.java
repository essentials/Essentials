package net.ess3.bukkit;

import lombok.Delegate;
import lombok.Getter;
import net.ess3.api.server.ItemStack;
import net.ess3.api.server.Location;
import net.ess3.api.server.World;
import org.bukkit.TreeType;

public class BukkitWorld implements World {
	private interface Excludes
	{
		boolean generateTree(Location safeLocation, TreeType tree);
		
		ItemStack dropItem(Location loc, ItemStack stack);
		
		Location getSpawnLocation();
		
		void dropItemNaturally(Location location, ItemStack overflowStack);
	}
	@Delegate(excludes = Excludes.class)
	@Getter
	private final org.bukkit.World bukkitWorld;

	public BukkitWorld(final org.bukkit.World world)
	{
		this.bukkitWorld = world;
	}

	@Override
	public boolean generateTree(Location safeLocation, TreeType tree)
	{
		return bukkitWorld.generateTree(((BukkitLocation)safeLocation).getBukkitLocation(), tree);
	}

	@Override
	public ItemStack dropItem(Location loc, ItemStack stack)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public Location getSpawnLocation()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void dropItemNaturally(Location location, ItemStack overflowStack)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}
}
