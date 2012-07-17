package net.ess3.geoip;

import net.ess3.api.IEssentials;
import net.ess3.settings.geoip.GeoIP;
import net.ess3.storage.AsyncStorageObjectHolder;
import java.io.File;
import java.io.IOException;
import org.bukkit.plugin.Plugin;


public class ConfigHolder extends AsyncStorageObjectHolder<GeoIP>
{
	private final transient Plugin geoip;

	public ConfigHolder(final IEssentials ess, final Plugin geoip)
	{
		super(ess, GeoIP.class);
		this.geoip = geoip;
		onReload();
	}

	@Override
	public File getStorageFile() throws IOException
	{
		return new File(geoip.getDataFolder(), "config.yml");
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
