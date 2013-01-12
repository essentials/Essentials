package net.ess3.api;

import java.io.File;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;


public interface IPlugin extends Plugin
{

	/**
	 * Get an instance of essentials
	 *
	 * @return
	 */
	IEssentials getEssentials();

	/**
	 * Schedule an a-sync task
	 *
	 * @param run - Code to call later
	 * @return - BukkitTask for the task created
	 */
	BukkitTask scheduleAsyncDelayedTask(final Runnable run);

	/**
	 * Schedule a sync task (ran in main thread) to be run
	 *
	 * @param run - Code to be run later
	 * @return - Integer for the task id
	 */
	int scheduleSyncDelayedTask(final Runnable run);

	/**
	 * Call an a-sync task to be run with a given delay
	 *
	 * @param run   - Code to be run
	 * @param delay - Long that represents how long to wait
	 * @return - BukkitTask for the task created
	 */
	BukkitTask scheduleAsyncDelayedTask(final Runnable run, final long delay);

	/**
	 * Schedule a sync (ran in main thread) delayed task
	 *
	 * @param run   - Code to run
	 * @param delay - Long that represents how long to wait
	 * @return - Integer of the task ID
	 */
	int scheduleSyncDelayedTask(final Runnable run, final long delay);

	/**
	 * Schedule a sync (in the main thread) repeating task
	 *
	 * @param run    - Code to run
	 * @param delay  - Delay for the first run
	 * @param period - Time to wait between every run after the first
	 * @return - int of the task ID
	 */
	int scheduleSyncRepeatingTask(final Runnable run, final long delay, final long period);

	/**
	 * Schedule an a-sync repeating task
	 *
	 * @param run    - Code to run
	 * @param delay  - Delay for the first run
	 * @param period - Time to wait between every run after the first
	 * @return - int of the task ID
	 */
	BukkitTask scheduleAsyncRepeatingTask(final Runnable run, final long delay, final long period);

	File getRootFolder();

	/**
	 * Stop a running task from a task id
	 *
	 * @param taskId
	 */
	void cancelTask(final int taskId);

	/**
	 * Stop a running task from a bukkit task
	 *
	 * @param taskId
	 */
	void cancelTask(final BukkitTask taskId);

	/**
	 * Get the essentials version
	 *
	 * @return
	 */
	String getVersion();

	/**
	 * Load a class, currently needs updating
	 *
	 * @param name - class name
	 * @return - loaded class
	 */
	Class<?> getClassByName(final String name);

	/**
	 * Call a re-spawn event on a player
	 *
	 * @param player   - Player to re-spawn
	 * @param loc      - Location to send
	 * @param bedSpawn - do you use bed?
	 * @return - Location after event called
	 */
	Location callRespawnEvent(Player player, Location loc, boolean bedSpawn);

	/**
	 * Call a suicide event on a player
	 *
	 * @param player - Player to kill
	 */
	void callSuicideEvent(Player player);

	/**
	 * Finds if an essentials module is loaded
	 *
	 * @param name
	 * @return
	 */
	public boolean isModuleEnabled(String name);

	public void onPluginEnable(Plugin plugin);

	public void onPluginDisable(Plugin plugin);

	/**
	 * Register a module with Essentials
	 *
	 * @param module - Your plugin instance
	 */
	void registerModule(Plugin module);

}
