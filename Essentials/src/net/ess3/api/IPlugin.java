package net.ess3.api;

import java.io.File;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public interface IPlugin extends Plugin {
	int scheduleAsyncDelayedTask(final Runnable run);

	int scheduleSyncDelayedTask(final Runnable run);

	int scheduleSyncDelayedTask(final Runnable run, final long delay);

	int scheduleSyncRepeatingTask(final Runnable run, final long delay, final long period);

	int scheduleAsyncRepeatingTask(final Runnable run, final long delay, final long period);

	File getRootFolder();

	void cancelTask(final int taskId);

	String getVersion();

	Class getClassByName(final String name);

	Location callRespawnEvent(Player player, Location loc, boolean bedSpawn);

	void callSuicideEvent(Player player);
}
