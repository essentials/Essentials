package net.ess3.api;

import net.ess3.api.server.Location;
import java.io.File;
import java.util.Collection;


public interface IWarps extends IReload
{
	Location getWarp(String warp) throws Exception;

	Collection<String> getList();

	void removeWarp(String name) throws Exception;

	void setWarp(String name, Location loc) throws Exception;

	public boolean isEmpty();

	public File getWarpFile(String name) throws InvalidNameException;
}
