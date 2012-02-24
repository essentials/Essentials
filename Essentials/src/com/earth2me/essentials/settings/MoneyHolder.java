package com.earth2me.essentials.settings;

import com.earth2me.essentials.api.IContext;
import com.earth2me.essentials.storage.AsyncStorageObjectHolder;
import java.io.File;
import java.io.IOException;


public class MoneyHolder extends AsyncStorageObjectHolder<Money>
{
	public MoneyHolder(IContext ess)
	{
		super(ess, Money.class);
		onReload();
	}

	@Override
	public File getStorageFile() throws IOException
	{
		return new File(context.getDataFolder(), "economy_npcs.yml");
	}
}
