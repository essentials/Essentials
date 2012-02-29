package com.earth2me.essentials.components.warps;

import com.earth2me.essentials.api.IContext;
import com.earth2me.essentials.api.IEssentials;
import com.earth2me.essentials.storage.MultiStorageComponent;
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import lombok.Cleanup;


public final class WarpsComponent extends MultiStorageComponent<Warp, IEssentials> implements IWarpsComponent
{
	public WarpsComponent(IContext context, IEssentials plugin)
	{
		super(context);
	}

	@Override
	public String getContainerId()
	{
		return "warps";
	}

	@Override
	public Warp getWarp(final String name)
	{
		@Cleanup
		IWarpComponent component = new WarpComponent(getContext(), getContext().getEssentials());
		loadFile(component, getFile(name));
		component.acquireReadLock();
		return component.getData();
	}

	@Override
	public boolean containsWarp(final String name)
	{
		return isPersistent(name);
	}

	@Override
	public void setWarp(String name, Warp warp)
	{
		@Cleanup
		IWarpComponent component = new WarpComponent(getContext(), getContext().getEssentials());
		loadFile(component, getFile(name));
		component.acquireWriteLock();
		component.setData(warp);
	}

	@Override
	public boolean removeWarp(String name)
	{
		if (isPersistent(name))
		{
			getFile(name).delete();
			return true;
		}
		else
		{
			return false;
		}
	}

	@Override
	public List<String> getList()
	{
		final File folder = ensureFolder();
		if (folder == null)
		{
			return Collections.emptyList();
		}

		final File[] files = folder.listFiles(new FilenameFilter()
		{
			@Override
			public boolean accept(File file, String string)
			{
				return string.toLowerCase(Locale.ENGLISH).endsWith(".yml");
			}
		});

		List<String> warps = new ArrayList<String>(files.length);
		for (int i = 0; i < files.length; i++)
		{
			if (files[i].isFile() && files[i].canRead())
			{
				final String name = files[i].getName();
				final int ext = name.lastIndexOf('.');
				if (ext > 1)
				{
					warps.add(name.substring(0, ext - 1).intern());
				}
				else
				{
					warps.add(name);
				}
			}
		}

		return Collections.unmodifiableList(warps);
	}

	@Override
	public boolean isEmpty()
	{
		return ensureFolder().listFiles(new FilenameFilter()
		{
			@Override
			public boolean accept(File file, String string)
			{
				return string.toLowerCase(Locale.ENGLISH).endsWith(".yml");
			}
		}).length == 0;
	}
}
