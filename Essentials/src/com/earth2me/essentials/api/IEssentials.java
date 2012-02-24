package com.earth2me.essentials.api;

import com.earth2me.essentials.components.users.IUser;
import com.earth2me.essentials.listener.TntExplodeListener;
import org.bukkit.plugin.Plugin;


/**
 * Outlines the main plugin.
 */
public interface IEssentials extends Plugin
{
	void addReloadListener(IReloadable listener);

	int broadcastMessage(IUser sender, String message);

	int scheduleAsyncDelayedTask(Runnable run);

	int scheduleSyncDelayedTask(Runnable run);

	int scheduleSyncDelayedTask(Runnable run, long delay);

	int scheduleSyncRepeatingTask(Runnable run, long delay, long period);

	void reload();

	TntExplodeListener getTntListener();

	void setGroups(IGroupsComponent groups);

	void removeReloadListener(IReloadable groups);
	
	IContext getContext();
}
