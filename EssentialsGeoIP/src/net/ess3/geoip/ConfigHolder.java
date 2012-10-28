package net.ess3.geoip;

import java.io.File;
import net.ess3.api.IEssentials;
import net.ess3.settings.geoip.GeoIP;
import net.ess3.storage.AsyncStorageObjectHolder;
import org.bukkit.plugin.Plugin;


public class ConfigHolder extends AsyncStorageObjectHolder<GeoIP>
{
	private final transient Plugin geoip;
	
	public ConfigHolder(final IEssentials ess, final Plugin geoip)
	{
		super(ess, GeoIP.class, new File(geoip.getDataFolder(), "config.yml"));
		this.geoip = geoip;
		onReload(true);
	}
}
