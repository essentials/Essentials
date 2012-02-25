package com.earth2me.essentials.components.settings;

import com.earth2me.essentials.api.IContext;
import com.earth2me.essentials.storage.StorageComponent;
import java.io.File;
import java.io.IOException;


public class MoneyHolder extends StorageComponent<Money>
{
	public MoneyHolder(IContext ess)
	{
		super(ess, Money.class);
		reload();
	}

	@Override
	public File getStorageFile() throws IOException
	{
		return new File(getContext().getDataFolder(), "economy_npcs.yml");
	}
}
