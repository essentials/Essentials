package com.earth2me.essentials.components.warps;

import com.earth2me.essentials.api.IContext;
import com.earth2me.essentials.api.IEssentials;
import com.earth2me.essentials.storage.StorageComponent;


public final class WarpComponent extends StorageComponent<Warp, IEssentials> implements IWarpComponent
{
	public WarpComponent(final IContext context, final IEssentials plugin)
	{
		super(context, Warp.class, plugin);
	}

	@Override
	public String getContainerId()
	{
		return getData().getName();
	}
}
