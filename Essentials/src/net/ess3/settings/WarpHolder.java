package net.ess3.settings;

import net.ess3.api.IEssentials;
import net.ess3.api.IWarp;
import net.ess3.api.InvalidNameException;
import net.ess3.storage.AsyncStorageObjectHolder;


public class WarpHolder extends AsyncStorageObjectHolder<Warp> implements IWarp
{
	@Override
	public void finishRead()
	{
	}

	@Override
	public void finishWrite()
	{
	}
	private final String name;

	public WarpHolder(String name, IEssentials ess) throws InvalidNameException
	{
		super(ess, Warp.class, ess.getWarps().getWarpFile(name));
		this.name = name;
		onReload();
	}
}
