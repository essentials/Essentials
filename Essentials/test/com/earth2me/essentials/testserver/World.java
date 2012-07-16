package com.earth2me.essentials.testserver;

import net.ess3.api.server.Location;
import org.bukkit.TreeType;

public class World implements World {

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

}
