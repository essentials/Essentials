package com.earth2me.essentials.components.settings.warps;

import com.earth2me.essentials.api.IContext;
import com.earth2me.essentials.api.IEssentials;
import com.earth2me.essentials.storage.StorageComponent;


public class WarpComponent extends StorageComponent<Warp, IEssentials> implements IWarpComponent
{
	private final String name;

	public WarpComponent(String name, IContext context, IEssentials plugin)
	{
		super(context, Warp.class, plugin);

		this.name = name;
	}

	@Override
	public String getContainerId()
	{
		return "warps/" + name;
	}
}
