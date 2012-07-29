package net.ess3.api.server;

import java.io.File;
import java.io.InputStream;
import java.util.logging.Logger;


public interface Plugin
{
	int scheduleAsyncDelayedTask(final Runnable run);

	int scheduleSyncDelayedTask(final Runnable run);

	int scheduleSyncDelayedTask(final Runnable run, final long delay);

	int scheduleSyncRepeatingTask(final Runnable run, final long delay, final long period);
	
	int scheduleAsyncRepeatingTask(final Runnable run, final long delay, final long period);

	File getRootFolder();
	
	File getDataFolder();

	void cancelTask(int taskId);

	String getVersion();
	
	Class getClassByName(String name);

	InputStream getResource(String string);
	
	Location callRespawnEvent(Player player, Location loc, boolean bedSpawn);
	
	void callSuicideEvent(Player player);
	
	Logger getLogger();
	
	Server getServer();
}
