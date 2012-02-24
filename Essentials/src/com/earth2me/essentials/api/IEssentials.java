package com.earth2me.essentials.api;

import com.earth2me.essentials.listener.TntExplodeListener;
import org.bukkit.plugin.Plugin;


/**
 * Outlines the main plugin.
 */
public interface IEssentials extends Plugin, IReloadable
{
	TntExplodeListener getTntListener();
	
	IContext getContext();
}
