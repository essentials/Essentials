package com.earth2me.essentials.api;

import com.earth2me.essentials.components.users.IUser;
import com.earth2me.essentials.listener.TntExplodeListener;
import org.bukkit.plugin.Plugin;


/**
 * Outlines the main plugin.
 */
public interface IEssentials extends Plugin
{
	int broadcastMessage(IUser sender, String message);

	void reload();

	TntExplodeListener getTntListener();
	
	IContext getContext();
}
