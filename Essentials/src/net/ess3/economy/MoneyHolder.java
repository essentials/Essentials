package net.ess3.economy;

import java.io.File;
import java.io.IOException;
import net.ess3.api.IEssentials;
import net.ess3.storage.AsyncStorageObjectHolder;


public class MoneyHolder extends AsyncStorageObjectHolder<Money>
{

	@Override
	public void finishRead()
	{
		
	}

	@Override
	public void finishWrite()
	{
		
	}
	
	
	public MoneyHolder(IEssentials ess)
	{
		super(ess, Money.class);
		onReload();
	}

	@Override
	public File getStorageFile() throws IOException
	{
		return new File(ess.getDataFolder(), "economy_npcs.yml");
	}
}
