package com.earth2me.essentials.economy;

import com.earth2me.essentials.api.IEssentials;
import com.earth2me.essentials.storage.AsyncStorageObjectHolder;
import java.io.File;
import java.io.IOException;


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
