package com.earth2me.essentials.protect.settings;

import com.earth2me.essentials.api.IContext;
import com.earth2me.essentials.protect.IEssentialsProtectPlugin;
import com.earth2me.essentials.storage.StorageComponent;


public class ProtectSettingsComponent extends StorageComponent<Protect>
{
	public ProtectSettingsComponent(IContext context)
	{
		super(context, Protect.class);
	}

	@Override
	public String getContainerId()
	{
		return "protect";
	}
}
