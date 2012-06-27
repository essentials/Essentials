package com.earth2me.essentials.signs;

import com.earth2me.essentials.api.IEssentials;
import com.earth2me.essentials.storage.AsyncStorageObjectHolder;
import java.io.File;
import java.io.IOException;
import java.util.*;
import org.bukkit.plugin.Plugin;


public class SignsConfigHolder extends AsyncStorageObjectHolder<SignsConfig>
{
	private final transient Plugin plugin;
	private Set<EssentialsSign> enabledSigns = new HashSet<EssentialsSign>();
	private boolean signsEnabled = false;

	public SignsConfigHolder(final IEssentials ess, final Plugin plugin)
	{
		super(ess, SignsConfig.class);
		this.plugin = plugin;
		onReload();
		acquireReadLock();
		try
		{
			final Map<String, Boolean> signs = getData().getSigns();
			for (Map.Entry<String, Boolean> entry : signs.entrySet())
			{
				if (entry.getKey().trim().toUpperCase(Locale.ENGLISH).equals("COLOR") || entry.getKey().trim().toUpperCase(Locale.ENGLISH).equals("COLOUR"))
				{
					signsEnabled = true;
					continue;
				}
				final Signs sign = Signs.valueOf(entry.getKey().toUpperCase(Locale.ENGLISH));
				if (sign != null && entry.getValue())
				{
					enabledSigns.add(sign.getSign());
				}
			}
		}
		finally
		{
			unlock();
		}
		acquireWriteLock();
		try
		{
			final Map<String, Boolean> signs = new HashMap<String, Boolean>();
			for (Signs sign : Signs.values())
			{
				signs.put(sign.toString(), enabledSigns.contains(sign.getSign()));
			}
			getData().setSigns(signs);
		}
		finally
		{
			unlock();
		}
	}

	@Override
	public File getStorageFile() throws IOException
	{
		return new File(plugin.getDataFolder(), "config.yml");
	}

	public Set<EssentialsSign> getEnabledSigns()
	{
		return enabledSigns;
	}

	public boolean areSignsDisabled()
	{
		return !signsEnabled;
	}

	@Override
	public void finishRead()
	{
	}

	@Override
	public void finishWrite()
	{
	}
}
