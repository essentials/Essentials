package com.earth2me.essentials.api;

import com.earth2me.essentials.components.IComponentList;
import com.earth2me.essentials.components.IComponentPlugin;
import com.earth2me.essentials.listeners.TntExplodeListener;
import org.bukkit.plugin.Plugin;


/**
 * Outlines the main plugin.
 */
public interface IEssentials extends IComponentPlugin
{
	TntExplodeListener getTntListener();

	IContext getContext();
}
