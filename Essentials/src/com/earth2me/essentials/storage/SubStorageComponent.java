package com.earth2me.essentials.storage;

import com.earth2me.essentials.api.IContext;
import org.bukkit.plugin.Plugin;



public abstract class SubStorageComponent<T extends IStorageObject, U extends Plugin> extends StorageComponent<T, U> implements ISubStorageComponent<T, U>
{
	private transient String containerId;

	protected SubStorageComponent(IContext context, Class<T> type, U plugin)
	{
		super(context, type, plugin);
	}

	@Override
	public String getContainerId()
	{
		return containerId;
	}

	@Override
	public void setContainerId(String containerId)
	{
		this.containerId = containerId;
	}
}
