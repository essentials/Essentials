package com.earth2me.essentials.api;

import java.io.File;
import java.util.Collection;

import com.earth2me.essentials.commands.WarpNotFoundException;
import org.bukkit.Location;


public interface IWarps
{
	/**
	 * Get a warp by name
	 *
	 * @param warp - Warp name
	 * @return - Location the warp is set to
	 * @throws WarpNotFoundException When the warp is not found
	 * @throws InvalidWorldException When the world the warp is in is not found
	 */
	Location getWarp(String warp) throws WarpNotFoundException, InvalidWorldException;

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
	 * @param loc - Location of warp
	 * @throws Exception
	 */
	void setWarp(String name, Location loc) throws Exception;

	/**
	 * Check to see if the file is empty
	 *
	 * @return
	 */
	boolean isEmpty();

	/**
	 * Get a warp file
	 * note: this is not yet implemented, as 3.x uses different storage methods
	 *
	 * @param name - name of file
	 * @return - an instance of the file
	 * @throws InvalidNameException - When the file is not found
	 */
	File getWarpFile(String name) throws InvalidNameException;

}
