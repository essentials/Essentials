package com.earth2me.essentials.geoip;

import static com.earth2me.essentials.components.i18n.I18nComponent._;
import com.earth2me.essentials.api.IContext;
import java.util.logging.Level;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;


public class EssentialsGeoIP extends JavaPlugin
{
	public EssentialsGeoIP()
	{
	}

	@Override
	public void onDisable()
	{
	}

	@Override
	public void onEnable()
	{
		final PluginManager pm = getServer().getPluginManager();
		final IContext ess = (IContext)pm.getPlugin("Essentials3");
		if (!this.getDescription().getVersion().equals(ess.getDescription().getVersion()))
		{
			getLogger().log(Level.WARNING, _("versionMismatchAll"));
		}
		if (!ess.isEnabled()) {
			this.setEnabled(false);
			return;
		}
		final EssentialsGeoIPPlayerListener playerListener = new EssentialsGeoIPPlayerListener(this, ess);
		pm.registerEvents(playerListener, this);
		ess.addReloadListener(playerListener);

		getLogger().log(Level.INFO, "This product includes GeoLite data created by MaxMind, available from http://www.maxmind.com/.");
	}
}
