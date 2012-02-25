package com.earth2me.essentials.signs;

import com.earth2me.essentials.api.IContext;
import com.earth2me.essentials.storage.StorageComponent;
import java.io.File;
import java.io.IOException;
import java.util.*;
import org.bukkit.plugin.Plugin;


public class SignsConfigHolder extends StorageComponent<SignsConfig, EssentialsSignsPlugin>
{
	private Set<EssentialsSign> enabledSigns = new HashSet<EssentialsSign>();

	public SignsConfigHolder(final IContext ess, final EssentialsSignsPlugin plugin)
	{
		super(ess, SignsConfig.class, plugin);
	}

	@Override
	public void onEnable()
	{
		// Call this FIRST.
		super.onEnable();

		acquireReadLock();
		try
		{
			Map<String, Boolean> signs = getData().getSigns();
			for (Map.Entry<String, Boolean> entry : signs.entrySet())
			{
				Signs sign = Signs.valueOf(entry.getKey().toUpperCase(Locale.ENGLISH));
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
			Map<String, Boolean> signs = new HashMap<String, Boolean>();
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

	public Set<EssentialsSign> getEnabledSigns()
	{
		return Collections.unmodifiableSet(enabledSigns);
	}
}
