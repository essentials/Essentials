package com.earth2me.essentials.protect;

import com.earth2me.essentials.api.EssentialsPlugin;
import com.earth2me.essentials.protect.data.IProtectedBlock;
import com.mchange.v2.log.MLog;
import com.mchange.v2.log.MLogger;
import java.util.logging.Filter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import org.bukkit.plugin.PluginManager;


public class EssentialsProtectPlugin extends EssentialsPlugin implements IEssentialsProtectPlugin
{
	private static MLogger C3P0logger;
	private transient IProtectedBlock storage = null;
	private transient ProtectSettingsComponent settings = null;

	@Override
	public void onLoad()
	{
		C3P0logger = MLog.getLogger(com.mchange.v2.c3p0.impl.AbstractPoolBackedDataSource.class);
		C3P0logger.setFilter(new Filter()
		{
			@Override
			public boolean isLoggable(LogRecord lr)
			{
				return lr.getLevel() != Level.INFO;
			}
		});
	}

	@Override
	public void onEnable()
	{
		// Call this FIRST.
		super.onEnable();

		final PluginManager pluginManager = this.getServer().getPluginManager();
		pluginManager.registerEvents(new EssentialsProtectPlayerListener(this), this);
		pluginManager.registerEvents(new EssentialsProtectBlockListener(this), this);
		pluginManager.registerEvents(new EssentialsProtectEntityListener(this), this);
		pluginManager.registerEvents(new EssentialsProtectWeatherListener(this), this);
	}

	@Override
	public IProtectedBlock getStorage()
	{
		return storage;
	}

	@Override
	public void setStorage(IProtectedBlock pb)
	{
		storage = pb;
	}

	@Override
	public void onDisable()
	{
		if (storage != null)
		{
			storage.onPluginDeactivation();
		}

		// TODO Less hax pl0x.
		// Sleep for a second to allow the database to close.
		try
		{
			Thread.sleep(1000);
		}
		catch (InterruptedException ex)
		{
		}
	}

	@Override
	public ProtectSettingsComponent getSettings()
	{
		return settings;
	}

	@Override
	public void setSettings(final ProtectSettingsComponent settings)
	{
		this.settings = settings;
	}
}
