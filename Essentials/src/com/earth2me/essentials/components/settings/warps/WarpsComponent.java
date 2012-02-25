package com.earth2me.essentials.components.settings.warps;

import com.earth2me.essentials.api.IContext;
import com.earth2me.essentials.api.IEssentials;
import com.earth2me.essentials.api.InvalidNameException;
import com.earth2me.essentials.components.warps.IWarp;
import com.earth2me.essentials.storage.StorageComponent;
import java.io.File;
import java.io.IOException;


public class WarpsComponent extends StorageComponent<Warp, IEssentials> implements IWarp
{
	// TODO Should this be worldName?  I'm unsure if it's the world.
	private final String name;

	public WarpsComponent(String name, IContext context, IEssentials plugin)
	{
		super(context, Warp.class, plugin);

		this.name = name;
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
