package net.ess3.bukkit;

import net.ess3.api.server.Player;
import net.ess3.api.server.Plugin;
import net.ess3.api.server.Location;
import java.io.File;
import lombok.Delegate;
import lombok.Getter;
import net.ess3.api.server.Server;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.java.JavaPluginLoader;


public class BukkitPlugin implements Plugin
{
	private interface Excludes {
		public org.bukkit.Server getServer();
	}
	@Delegate(excludes={Excludes.class})
	@Getter
	private final org.bukkit.plugin.Plugin bukkitPlugin;
	@Getter
	private final Server server;

	public BukkitPlugin(final org.bukkit.plugin.Plugin plugin, final Server server)
	{
		this.bukkitPlugin = plugin;
		this.server = server;
	}
	
	@Override
	public int scheduleAsyncDelayedTask(final Runnable run)
	{
		return bukkitPlugin.getServer().getScheduler().scheduleAsyncDelayedTask(bukkitPlugin, run);
	}

	@Override
	public int scheduleSyncDelayedTask(final Runnable run)
	{
		return bukkitPlugin.getServer().getScheduler().scheduleSyncDelayedTask(bukkitPlugin, run);
	}

	@Override
	public int scheduleSyncDelayedTask(final Runnable run, final long delay)
	{
		return bukkitPlugin.getServer().getScheduler().scheduleSyncDelayedTask(bukkitPlugin, run, delay);
	}

	@Override
	public int scheduleSyncRepeatingTask(final Runnable run, final long delay, final long period)
	{
		return bukkitPlugin.getServer().getScheduler().scheduleSyncRepeatingTask(bukkitPlugin, run, delay, period);
	}
	
	@Override
	public int scheduleAsyncRepeatingTask(final Runnable run, final long delay, final long period)
	{
		return bukkitPlugin.getServer().getScheduler().scheduleAsyncRepeatingTask(bukkitPlugin, run, delay, period);
	}
	
	@Override
	public File getRootFolder()
	{
		return bukkitPlugin.getDataFolder().getParentFile().getParentFile();
	}

	@Override
	public void cancelTask(final int taskId)
	{
		bukkitPlugin.getServer().getScheduler().cancelTask(taskId);
	}

	@Override
	public String getVersion()
	{
		return bukkitPlugin.getDescription().getVersion();
	}

	@Override
	public Class getClassByName(final String name)
	{
		final JavaPluginLoader jpl = (JavaPluginLoader)bukkitPlugin.getPluginLoader();
		return jpl.getClassByName(name);
	}

	@Override
	public Location callRespawnEvent(Player player, Location loc, boolean bedSpawn)
	{
		final PlayerRespawnEvent pre = new PlayerRespawnEvent(((BukkitPlayer)player).getOnlinePlayer(), ((BukkitLocation)loc).getBukkitLocation() , bedSpawn);
		getBukkitServer().getPluginManager().callEvent(pre);
		return new BukkitLocation(pre.getRespawnLocation());
	}

	@Override
	public void callSuicideEvent(Player player)
	{
		EntityDamageEvent ede = new EntityDamageEvent(((BukkitPlayer)player).getOnlinePlayer(), EntityDamageEvent.DamageCause.SUICIDE, 1000);
		getBukkitServer().getPluginManager().callEvent(ede);
	}
	
	public org.bukkit.Server getBukkitServer()
	{
		return bukkitPlugin.getServer();
	}
}
