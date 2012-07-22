package net.ess3.bukkit;

import net.ess3.api.server.Player;
import net.ess3.api.server.Plugin;
import net.ess3.api.server.Location;
import java.io.File;
import lombok.Delegate;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.java.JavaPluginLoader;


public class BukkitPlugin implements Plugin
{
	@Delegate
	private final org.bukkit.plugin.Plugin plugin;

	public BukkitPlugin(final org.bukkit.plugin.Plugin plugin)
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
	public int scheduleAsyncRepeatingTask(final Runnable run, final long delay, final long period)
	{
		return plugin.getServer().getScheduler().scheduleAsyncRepeatingTask(plugin, run, delay, period);
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
