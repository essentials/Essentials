package net.ess3.bukkit;

import net.ess3.api.server.Block;
import net.ess3.api.server.ItemStack;
import net.ess3.api.server.Location;

public class BukkitBlockFactory {

	public static Block convert(org.bukkit.block.Block block) {
		Location loc = Location.create(block.getWorld().getName(), block.getX(), block.getY(), block.getZ());
		return new Block(ItemStack.create(block.getTypeId(), 1, block.getData()), loc);
	}
}
