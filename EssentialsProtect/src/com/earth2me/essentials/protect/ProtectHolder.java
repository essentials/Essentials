package com.earth2me.essentials.protect;

import net.ess3.api.IEssentials;
import net.ess3.settings.protect.Protect;
import net.ess3.storage.AsyncStorageObjectHolder;
import java.io.File;
import java.io.IOException;


public class ProtectHolder extends AsyncStorageObjectHolder<Protect>
{
	public ProtectHolder(IEssentials ess)
	{
		super(ess, Protect.class);
	}

	@Override
	public File getStorageFile() throws IOException
	{
		return new File(ess.getDataFolder(), "protect.yml");
	}
}
