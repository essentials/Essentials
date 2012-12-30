package net.ess3.bukkit;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import net.ess3.Essentials;
import static net.ess3.I18n._;
import net.ess3.api.IEssentials;
import net.ess3.api.IPlugin;
import net.ess3.listener.EssentialsBlockListener;
import net.ess3.listener.EssentialsEntityListener;
import net.ess3.listener.EssentialsPlayerListener;
import net.ess3.listener.EssentialsPluginListener;
import net.ess3.metrics.MetricsListener;
import net.ess3.metrics.MetricsStarter;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;
import org.bukkit.scheduler.BukkitTask;


public class BukkitPlugin extends JavaPlugin implements IPlugin
{
	private Essentials ess;
	private Map<String, Plugin> modules = Collections.synchronizedMap(new HashMap<String, Plugin>());

	@Override
	public void onEnable()
	{

		final PluginManager pm = this.getServer().getPluginManager();
		//pm.registerEvents(getServer(), this);
		ess = new Essentials(getServer(), getLogger(), this);
		if (VersionCheck.checkVersion(this))
		{
			try
			{
				ess.onEnable();
			}
			catch (RuntimeException ex)
			{
				if (pm.getPlugin("EssentialsUpdate") == null)
				{
					getLogger().log(Level.SEVERE, _("essentialsHelp1"));
				}
				else
				{
					getLogger().log(Level.SEVERE, _("essentialsHelp2"));
				}
				getLogger().log(Level.SEVERE, ex.toString());
				pm.registerEvents(new Listener()
				{
					@EventHandler(priority = EventPriority.LOW)
					public void onPlayerJoin(final PlayerJoinEvent event)
					{
						event.getPlayer().sendMessage("Essentials failed to load, read the log file.");
					}
				}, this);
				for (Player player : getServer().getOnlinePlayers())
				{
					player.sendMessage("Essentials failed to load, read the log file.");
				}
				this.setEnabled(false);
				return;
			}
		}
		else
		{
			this.setEnabled(false);
			return;
		}

		final EssentialsPluginListener serverListener = new EssentialsPluginListener(ess);
		pm.registerEvents(serverListener, this);
		ess.addReloadListener(serverListener);

		final EssentialsPlayerListener playerListener = new EssentialsPlayerListener(ess);
		pm.registerEvents(playerListener, this);

		final EssentialsBlockListener blockListener = new EssentialsBlockListener(ess);
		pm.registerEvents(blockListener, this);

		final EssentialsEntityListener entityListener = new EssentialsEntityListener(ess);
		pm.registerEvents(entityListener, this);


		final MetricsStarter metricsStarter = new MetricsStarter(ess);
		if (metricsStarter.getStart() != null && metricsStarter.getStart() == true)
		{
			getServer().getScheduler().runTaskLaterAsynchronously(this, metricsStarter, 1);
		}
		else if (metricsStarter.getStart() != null && metricsStarter.getStart() == false)
		{
			final MetricsListener metricsListener = new MetricsListener(ess, metricsStarter);
			pm.registerEvents(metricsListener, this);
		}
	}

	@Override
	public void onDisable()
	{
		if (ess != null)
		{
			ess.onDisable();
		}
	}

	@Override
	public boolean onCommand(final org.bukkit.command.CommandSender sender, final Command command, final String label, final String[] args)
	{
		return ess.getCommandHandler().onCommand(sender, command, label, args);
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args)
	{
		return ess.getCommandHandler().onTabComplete(sender, command, alias, args);
	}

	@Override
	public BukkitTask scheduleAsyncDelayedTask(final Runnable run)
	{
		return getServer().getScheduler().runTaskAsynchronously(this, run);
	}

	@Override
	public int scheduleSyncDelayedTask(final Runnable run)
	{
		return getServer().getScheduler().scheduleSyncDelayedTask(this, run);
	}

	@Override
	public BukkitTask scheduleAsyncDelayedTask(final Runnable run, final long delay)
	{
		return getServer().getScheduler().runTaskLaterAsynchronously(this, run, delay);
	}

	@Override
	public int scheduleSyncDelayedTask(final Runnable run, final long delay)
	{
		return getServer().getScheduler().scheduleSyncDelayedTask(this, run, delay);
	}

	@Override
	public int scheduleSyncRepeatingTask(final Runnable run, final long delay, final long period)
	{
		return getServer().getScheduler().scheduleSyncRepeatingTask(this, run, delay, period);
	}

	@Override
	public BukkitTask scheduleAsyncRepeatingTask(final Runnable run, final long delay, final long period)
	{
		return getServer().getScheduler().runTaskTimer(this, run, delay, period);
	}

	@Override
	public File getRootFolder()
	{
		return getDataFolder().getParentFile().getParentFile();
	}

	@Override
	public void cancelTask(final int taskId)
	{
		getServer().getScheduler().cancelTask(taskId);
	}
	
	@Override
	public void cancelTask(BukkitTask taskId) 
	{
		getServer().getScheduler().cancelTask(taskId.getTaskId());
	}
	
	@Override
	public String getVersion()
	{
		return getDescription().getVersion();
	}

	@Override
	public Class<?> getClassByName(final String name)
	{
		final JavaPluginLoader jpl = (JavaPluginLoader)this.getPluginLoader();
		return jpl.getClassByName(name);
	}

	@Override
	public Location callRespawnEvent(final Player player, final Location loc, final boolean bedSpawn)
	{
		final PlayerRespawnEvent pre = new PlayerRespawnEvent(player, loc, bedSpawn);
		getServer().getPluginManager().callEvent(pre);
		return pre.getRespawnLocation();
	}

	@Override
	public void callSuicideEvent(final Player player)
	{
		EntityDamageEvent ede = new EntityDamageEvent(player, EntityDamageEvent.DamageCause.SUICIDE, 1000);
		getServer().getPluginManager().callEvent(ede);
	}

	@Override
	public IEssentials getEssentials()
	{
		return ess;
	}

	@Override
	public boolean isModuleEnabled(final String name)
	{
		return modules.containsKey(name);
	}

	@Override
	public void onPluginEnable(final Plugin plugin)
	{
		if (plugin.getName().equals(this.getName())
			|| !plugin.getName().startsWith("Essentials"))
		{
			return;
		}
		// Remove "Essentials" from name
		modules.put(plugin.getName().substring(10), plugin);
	}
	
	@Override
	public void registerModule(Plugin module) 
	{ // TODO: Use, solution for L231
		modules.put(module.getName().substring(10), module);
	}

	@Override
	public void onPluginDisable(final Plugin plugin)
	{
		if (plugin.getName().equals(this.getName())
			|| !plugin.getName().startsWith("Essentials"))
		{
			return;
		}
		modules.remove(plugin.getName().substring(10));
	}
}
