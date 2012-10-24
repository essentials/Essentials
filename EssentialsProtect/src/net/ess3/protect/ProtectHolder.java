package net.ess3.protect;

import java.io.File;
import net.ess3.api.IEssentials;
import net.ess3.settings.protect.Protect;
import net.ess3.storage.AsyncStorageObjectHolder;


public class ProtectHolder extends AsyncStorageObjectHolder<Protect>
{
	public ProtectHolder(final IEssentials ess)
	{
		super(ess, Protect.class, new File(ess.getPlugin().getDataFolder(), "protect.yml"));
	}
}
