package net.ess3.testserver;

import java.util.UUID;
import net.ess3.api.server.ItemStack;
import net.ess3.api.server.Location;
import net.ess3.api.server.World;
import org.bukkit.TreeType;

public class TestWorld implements World {

	@Override
	public String getName()
	{
		return "TestWorld";
	}

	@Override
	public boolean generateTree(Location safeLocation, TreeType tree)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public int getHighestBlockYAt(int topX, int topZ)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public ItemStack dropItem(Location loc, ItemStack stack)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public UUID getUID()
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

	@Override
	public void setStorm(boolean b)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void setWeatherDuration(int i)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public long getTime()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public boolean setSpawnLocation(int blockX, int blockY, int blockZ)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

}
