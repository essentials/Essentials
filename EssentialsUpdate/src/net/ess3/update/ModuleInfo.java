package net.ess3.update;

import java.net.MalformedURLException;
import java.net.URL;
import org.bukkit.configuration.Configuration;


public class ModuleInfo
{
	private final String url;
	private final String version;
	private final String hash;

	public ModuleInfo(final Configuration updateConfig, final String path)
	{
		url = updateConfig.getString(path + ".url", null);
		version = updateConfig.getString(path + ".version", null);
		hash = updateConfig.getString(path + ".hash", null);
	}

	public URL getUrl() throws MalformedURLException
	{
		return new URL(url);
	}

	public String getVersion()
	{
		return version;
	}

	public String getHash()
	{
		return hash;
	}
}
