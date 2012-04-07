package com.earth2me.essentials.testserver;

import com.earth2me.essentials.api.server.IWorld;
import com.earth2me.essentials.api.server.Location;
import org.bukkit.TreeType;

public class World implements IWorld {

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
