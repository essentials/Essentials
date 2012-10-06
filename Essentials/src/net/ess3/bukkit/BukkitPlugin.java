package net.ess3.bukkit;

import java.io.File;
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
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;


public class BukkitPlugin extends JavaPlugin implements IPlugin
{	
	private Essentials ess;
	
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
			getServer().getScheduler().scheduleAsyncDelayedTask(this, metricsStarter, 1);
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
		return ess.getCommandHandler().handleCommand(sender, command, label, args);
	}
	
	@Override
	public int scheduleAsyncDelayedTask(final Runnable run)
	{
		return getServer().getScheduler().scheduleAsyncDelayedTask(this, run);
	}

	@Override
	public int scheduleSyncDelayedTask(final Runnable run)
	{
		return getServer().getScheduler().scheduleSyncDelayedTask(this, run);
	}
	
	@Override
	public int scheduleAsyncDelayedTask(final Runnable run, final long delay)
	{
		return getServer().getScheduler().scheduleAsyncDelayedTask(this, run, delay);
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
	public int scheduleAsyncRepeatingTask(final Runnable run, final long delay, final long period)
	{
		return getServer().getScheduler().scheduleAsyncRepeatingTask(this, run, delay, period);
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
	public String getVersion()
	{
		return getDescription().getVersion();
	}

	@Override
	public Class getClassByName(final String name)
	{
		final JavaPluginLoader jpl = (JavaPluginLoader)this.getPluginLoader();
		return jpl.getClassByName(name);
	}

	@Override
	public Location callRespawnEvent(Player player, Location loc, boolean bedSpawn)
	{
		final PlayerRespawnEvent pre = new PlayerRespawnEvent(player, loc, bedSpawn);
		getServer().getPluginManager().callEvent(pre);
		return pre.getRespawnLocation();
	}

	@Override
	public void callSuicideEvent(Player player)
	{
		EntityDamageEvent ede = new EntityDamageEvent(player, EntityDamageEvent.DamageCause.SUICIDE, 1000);
		getServer().getPluginManager().callEvent(ede);
	}

	@Override
	public IEssentials getEssentials()
	{
		return ess;
	}
}
