package com.earth2me.essentials;

import com.earth2me.essentials.api.IScheduler;
import org.bukkit.plugin.Plugin;


public class Scheduler implements IScheduler
{
	private transient final Plugin plugin;
	
	public Scheduler(Plugin plugin)
	{
		this.plugin = plugin;
	}
	
	@Override
	public int scheduleAsyncDelayedTask(final Runnable run)
	{
		return plugin.getServer().getScheduler().scheduleAsyncDelayedTask(plugin, run);
	}

	@Override
	public int scheduleSyncDelayedTask(final Runnable run)
	{
		return plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, run);
	}

	@Override
	public int scheduleSyncDelayedTask(final Runnable run, final long delay)
	{
		return plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, run, delay);
	}

	@Override
	public int scheduleSyncRepeatingTask(final Runnable run, final long delay, final long period)
	{
		return plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, run, delay, period);
	}
}
