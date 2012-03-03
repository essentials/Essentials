package com.earth2me.essentials.components.warps;

import com.earth2me.essentials.storage.IStorageComponentMap;
import com.earth2me.essentials.storage.LocationData;
import java.util.Set;
import org.bukkit.Location;


public interface IWarpsComponent extends IStorageComponentMap<IWarpComponent>
{
	Location getWarp(final String name) throws LocationData.WorldNotLoadedException;

	void setWarp(final String name, final Location warp);
	
	void setWarp(final String name, final LocationData warp);

	boolean removeWarp(final String name);

	boolean containsWarp(final String name);

	Set<String> getList();
}
