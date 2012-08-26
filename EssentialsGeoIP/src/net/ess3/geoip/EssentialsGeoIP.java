package net.ess3.geoip;

import static net.ess3.I18n._;
import net.ess3.api.IEssentials;
import java.util.logging.Level;
import net.ess3.api.IPlugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;


public class EssentialsGeoIP extends JavaPlugin
{
	@Override
	public void onEnable()
	{
		final PluginManager pm = getServer().getPluginManager();
		final IPlugin plugin = (IPlugin)pm.getPlugin("Essentials-3");
		final IEssentials ess = (IEssentials)plugin.getEssentials();
		if (!this.getDescription().getVersion().equals(plugin.getDescription().getVersion()))
		{
			getLogger().log(Level.WARNING, _("versionMismatchAll"));
		}
		if (!plugin.isEnabled())
		{
			this.setEnabled(false);
			return;
		}
		final EssentialsGeoIPPlayerListener playerListener = new EssentialsGeoIPPlayerListener(this, ess);
		pm.registerEvents(playerListener, this);
		ess.addReloadListener(playerListener);

		getLogger().log(Level.INFO, "This product includes GeoLite data created by MaxMind, available from http://www.maxmind.com/.");
	}
}
