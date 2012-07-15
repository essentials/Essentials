package com.earth2me.essentials.bukkit;

import com.earth2me.essentials.api.server.World;
import com.earth2me.essentials.api.server.ItemStack;
import com.earth2me.essentials.api.server.Location;
import lombok.Delegate;
import lombok.Getter;
import org.bukkit.TreeType;

public class BukkitWorld implements World {
	@Delegate
	@Getter
	private final org.bukkit.World bukkitWorld;

	public BukkitWorld(final org.bukkit.World world)
	{
		this.bukkitWorld = world;
		world.getT
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
