package com.earth2me.essentials.protect;

import com.earth2me.essentials.api.IContext;
import com.earth2me.essentials.components.settings.protect.Protect;
import com.earth2me.essentials.storage.StorageComponent;
import java.io.File;
import java.io.IOException;


public class ProtectHolder extends StorageComponent<Protect>
{
	public ProtectHolder(IContext ess)
	{
		super(ess, Protect.class);
	}

	@Override
	public File getStorageFile() throws IOException
	{
		return new File(getContext().getDataFolder(), "protect.yml");
	}
}
