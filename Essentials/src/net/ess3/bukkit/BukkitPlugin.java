package net.ess3.bukkit;

import java.io.File;
import java.util.logging.Level;
import net.ess3.Essentials;
import static net.ess3.I18n._;
import net.ess3.api.IPlugin;
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
			}
		}
		else
		{
			this.setEnabled(false);
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
}
