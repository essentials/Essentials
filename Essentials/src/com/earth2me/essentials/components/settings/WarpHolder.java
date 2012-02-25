package com.earth2me.essentials.components.settings;

import com.earth2me.essentials.api.IContext;
import com.earth2me.essentials.api.InvalidNameException;
import com.earth2me.essentials.components.warps.IWarp;
import com.earth2me.essentials.storage.StorageComponent;
import java.io.File;
import java.io.IOException;


public class WarpHolder extends StorageComponent<Warp> implements IWarp
{
	private final String name;

	public WarpHolder(String name, IContext ess)
	{
		super(ess, Warp.class);
		this.name = name;
		reload();
	}

	@Override
	public File getStorageFile() throws IOException
	{
		try
		{
			return getContext().getWarps().getWarpFile(name);
		}
		catch (InvalidNameException ex)
		{
			throw new IOException(ex.getMessage(), ex);
		}
	}

}
