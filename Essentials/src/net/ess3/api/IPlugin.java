package net.ess3.api;

import java.io.File;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

public interface IPlugin extends Plugin {
	IEssentials getEssentials();
	
	BukkitTask scheduleAsyncDelayedTask(final Runnable run);

	int scheduleSyncDelayedTask(final Runnable run);
	
	BukkitTask scheduleAsyncDelayedTask(final Runnable run, final long delay);

	int scheduleSyncDelayedTask(final Runnable run, final long delay);

	int scheduleSyncRepeatingTask(final Runnable run, final long delay, final long period);

	BukkitTask scheduleAsyncRepeatingTask(final Runnable run, final long delay, final long period);

	File getRootFolder();

	void cancelTask(final int taskId);

	String getVersion();

	Class<?> getClassByName(final String name);

	Location callRespawnEvent(Player player, Location loc, boolean bedSpawn);

	void callSuicideEvent(Player player);

	public boolean isModuleEnabled(String name);

	public void onPluginEnable(Plugin plugin);

	public void onPluginDisable(Plugin plugin);
}
