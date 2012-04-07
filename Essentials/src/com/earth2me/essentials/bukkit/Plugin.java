package com.earth2me.essentials.bukkit;

import com.earth2me.essentials.api.server.Player;
import com.earth2me.essentials.api.server.IPlugin;
import com.earth2me.essentials.api.server.Location;
import java.io.File;
import lombok.Delegate;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.java.JavaPluginLoader;


public class Plugin implements IPlugin
{
	@Delegate
	private final org.bukkit.plugin.Plugin plugin;

	public Plugin(final org.bukkit.plugin.Plugin plugin)
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
	
	@Override
	public File getRootFolder()
	{
		return plugin.getDataFolder().getParentFile().getParentFile();
	}

	@Override
	public void cancelTask(final int taskId)
	{
		plugin.getServer().getScheduler().cancelTask(taskId);
	}

	@Override
	public String getVersion()
	{
		return plugin.getDescription().getVersion();
	}

	@Override
	public Class getClassByName(final String name)
	{
		final JavaPluginLoader jpl = (JavaPluginLoader)plugin.getPluginLoader();
		return jpl.getClassByName(name);
	}

	@Override
	public Location callRespawnEvent(Player player, Location loc, boolean bedSpawn)
	{
		final PlayerRespawnEvent pre = new PlayerRespawnEvent(((BukkitPlayer)player).getOnlinePlayer(), ((BukkitLocation)loc).getBukkitLocation() , bedSpawn);
		getServer().getPluginManager().callEvent(pre);
		return new BukkitLocation(pre.getRespawnLocation());
	}

	@Override
	public void callSuicideEvent(Player player)
	{
		EntityDamageEvent ede = new EntityDamageEvent(((BukkitPlayer)player).getOnlinePlayer(), EntityDamageEvent.DamageCause.SUICIDE, 1000);
		getServer().getPluginManager().callEvent(ede);
	}
	
}
