package com.earth2me.essentials.api.server;

import java.io.File;
import java.io.InputStream;


public interface IPlugin
{
	int scheduleAsyncDelayedTask(final Runnable run);

	int scheduleSyncDelayedTask(final Runnable run);

	int scheduleSyncDelayedTask(final Runnable run, final long delay);

	int scheduleSyncRepeatingTask(final Runnable run, final long delay, final long period);

	File getRootFolder();
	
	File getDataFolder();

	void cancelTask(int taskId);

	String getVersion();
	
	Class getClassByName(String name);

	InputStream getResource(String string);
	
	Location callRespawnEvent(Player player, Location loc, boolean bedSpawn);
	
	void callSuicideEvent(Player player);
}
