package com.earth2me.essentials.api;

import com.earth2me.essentials.components.IComponent;
import com.earth2me.essentials.listeners.TntExplodeListener;
import java.util.List;
import org.bukkit.plugin.Plugin;


/**
 * Outlines the main plugin.
 */
public interface IEssentials extends List<IComponent>, Plugin, IReloadable
{
	TntExplodeListener getTntListener();

	IContext getContext();
}
