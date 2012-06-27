package net.ess3.protect;

import net.ess3.api.IEssentials;
import net.ess3.settings.protect.Protect;
import net.ess3.storage.AsyncStorageObjectHolder;
import java.io.File;
import java.io.IOException;


public class ProtectHolder extends AsyncStorageObjectHolder<Protect>
{
	public ProtectHolder(final IEssentials ess)
	{
		super(ess, Protect.class);
	}

	@Override
	public File getStorageFile() throws IOException
	{
		return new File(ess.getDataFolder(), "protect.yml");
	}

	@Override
	public void finishRead()
	{
	}

	@Override
	public void finishWrite()
	{
	}
}
