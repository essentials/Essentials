package com.earth2me.essentials.components;

import com.earth2me.essentials.api.IReloadable;
import lombok.Delegate;
import org.bukkit.plugin.java.JavaPlugin;


public abstract class ComponentPlugin extends JavaPlugin implements IComponentPlugin
{
	@Delegate(types={IComponentList.class})
	private transient final IComponentList components = new ComponentList();

	/**
	 * Reload all components.
	 */
	@Override
	public void reload()
	{
		for (IReloadable reloadable : this)
		{
			reloadable.reload();
		}
	}

	@Override
	public void onDisable()
	{
		components.clear();
	}
}
