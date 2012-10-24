package net.ess3.economy;

import java.io.File;
import net.ess3.api.IEssentials;
import net.ess3.storage.AsyncStorageObjectHolder;


public class MoneyHolder extends AsyncStorageObjectHolder<Money>
{
	public MoneyHolder(IEssentials ess)
	{
		super(ess, Money.class, new File(ess.getPlugin().getDataFolder(), "economy_npcs.yml"));
		onReload();
	}
}
