package net.ess3.economy;

import net.ess3.api.IEssentials;
import net.ess3.storage.AsyncStorageObjectHolder;
import java.io.File;
import java.io.IOException;


public class MoneyHolder extends AsyncStorageObjectHolder<Money>
{
	public MoneyHolder(IEssentials ess)
	{
		super(ess, Money.class);
		onReload();
	}

	@Override
	public File getStorageFile() throws IOException
	{
		return new File(ess.getPlugin().getDataFolder(), "economy_npcs.yml");
	}
}
