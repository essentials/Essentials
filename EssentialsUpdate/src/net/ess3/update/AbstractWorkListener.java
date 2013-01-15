package net.ess3.update;

import org.bukkit.plugin.Plugin;


public abstract class AbstractWorkListener
{
	public AbstractWorkListener(final Plugin plugin, final VersionInfo newVersionInfo)
	{
		this.plugin = plugin;
		this.newVersionInfo = newVersionInfo;
	}

	private final Plugin plugin;
	private final VersionInfo newVersionInfo;

	public final void onWorkAbort()
	{
		onWorkAbort(null);
	}

	public abstract void onWorkAbort(String message);

	public final void onWorkDone()
	{
		onWorkDone(null);
	}

	public abstract void onWorkDone(String message);

	public VersionInfo getNewVersionInfo()
	{
		return newVersionInfo;
	}

	public Plugin getPlugin()
	{
		return plugin;
	}
}
