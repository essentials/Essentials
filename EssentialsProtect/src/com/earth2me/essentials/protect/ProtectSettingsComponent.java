package com.earth2me.essentials.protect;

import com.earth2me.essentials.api.IContext;
import com.earth2me.essentials.components.settings.protect.Protect;
import com.earth2me.essentials.storage.StorageComponent;


public class ProtectSettingsComponent extends StorageComponent<Protect, IEssentialsProtectPlugin>
{
	public ProtectSettingsComponent(IContext context, final IEssentialsProtectPlugin plugin)
	{
		super(context, Protect.class, plugin);
	}

	@Override
	public String getContainerId()
	{
		return "protect";
	}
}
