package net.ess3.api;

import java.io.File;
import java.util.Collection;
import org.bukkit.Location;


public interface IWarps extends IReload
{
	/**
	 * Get a warp by name
	 *
	 * @param warp - Warp name
	 * @return - Location the warp is set to
	 * @throws Exception
	 */
	Location getWarp(String warp) throws Exception;

	/**
	 * Gets a list of warps
	 *
	 * @return - A {@link Collection} of warps
	 */
	Collection<String> getList();

	/**
	 * Delete a warp from the warp DB
	 *
	 * @param name - Name of warp
	 * @throws Exception
	 */
	void removeWarp(String name) throws Exception;

	/**
	 * Set a warp
	 *
	 * @param name - Name of warp
	 * @param loc  - Location of warp
	 * @throws Exception
	 */
	void setWarp(String name, Location loc) throws Exception;

	/**
	 * Check to see if the file is empty
	 *
	 * @return
	 */
	public boolean isEmpty();

	/**
	 * Get a warp file
	 *
	 * @param name - name of file
	 * @return - an instance of the file
	 * @throws InvalidNameException - When the file is not found
	 */
	public File getWarpFile(String name) throws InvalidNameException;
}
