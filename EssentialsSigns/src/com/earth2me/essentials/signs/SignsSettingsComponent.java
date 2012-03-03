package com.earth2me.essentials.signs;

import com.earth2me.essentials.api.IContext;
import com.earth2me.essentials.storage.StorageComponent;
import java.util.*;


public class SignsSettingsComponent extends StorageComponent<SignsConfig>
{
	private Set<EssentialsSign> enabledSigns = new HashSet<EssentialsSign>();

	public SignsSettingsComponent(final IContext ess)
	{
		super(ess, SignsConfig.class);
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

	@Override
	public String getContainerId()
	{
		return "signs";
	}
}
