package com.earth2me.essentials.components.settings.warps;

import com.earth2me.essentials.api.IContext;
import com.earth2me.essentials.api.IEssentials;
import com.earth2me.essentials.storage.StorageComponent;


public class WarpsComponent extends StorageComponent<Warp, IEssentials> implements IWarpsComponent
{
	public WarpsComponent(IContext context, IEssentials plugin)
	{
		super(context, Warp.class, plugin);
	}

	@Override
	public String getContainerId()
	{
		return "settings.warps";
	}
}
