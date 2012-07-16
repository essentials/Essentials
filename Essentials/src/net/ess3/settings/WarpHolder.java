package net.ess3.settings;

import net.ess3.api.IEssentials;
import net.ess3.api.IWarp;
import net.ess3.api.InvalidNameException;
import net.ess3.storage.AsyncStorageObjectHolder;
import java.io.File;
import java.io.IOException;


public class WarpHolder extends AsyncStorageObjectHolder<Warp> implements IWarp
{
	private final String name;

	public WarpHolder(String name, IEssentials ess)
	{
		super(ess, Warp.class);
		this.name = name;
		onReload();
	}

	@Override
	public File getStorageFile() throws IOException
	{
		try
		{
			return ess.getWarps().getWarpFile(name);
		}
		catch (InvalidNameException ex)
		{
			throw new IOException(ex.getMessage(), ex);
		}
	}
	
}
