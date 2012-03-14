package com.earth2me.essentials.anticheat;

import com.earth2me.essentials.anticheat.checks.WorkaroundsListener;
import com.earth2me.essentials.anticheat.checks.blockbreak.BlockBreakCheckListener;
import com.earth2me.essentials.anticheat.checks.blockplace.BlockPlaceCheckListener;
import com.earth2me.essentials.anticheat.checks.chat.ChatCheckListener;
import com.earth2me.essentials.anticheat.checks.fight.FightCheckListener;
import com.earth2me.essentials.anticheat.checks.inventory.InventoryCheckListener;
import com.earth2me.essentials.anticheat.checks.moving.MovingCheckListener;
import com.earth2me.essentials.anticheat.command.CommandHandler;
import com.earth2me.essentials.anticheat.config.ConfigurationCacheStore;
import com.earth2me.essentials.anticheat.config.ConfigurationManager;
import com.earth2me.essentials.anticheat.config.Permissions;
import com.earth2me.essentials.anticheat.data.PlayerManager;
import com.earth2me.essentials.anticheat.debug.ActiveCheckPrinter;
import com.earth2me.essentials.anticheat.debug.LagMeasureTask;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;


/**
 *
 * NoCheat
 *
 * Check various player events for their plausibility and log/deny them/react to them based on configuration
 */
public class NoCheat extends JavaPlugin implements Listener
{
	private ConfigurationManager conf;
	private CommandHandler commandHandler;
	private PlayerManager players = new PlayerManager(this);
	private List<EventManager> eventManagers = new ArrayList<EventManager>();
	private LagMeasureTask lagMeasureTask;
	private Logger fileLogger;

	@Override
	public void onDisable()
	{
		if (lagMeasureTask != null)
		{
			lagMeasureTask.cancel();
		}

		if (conf != null)
		{
			conf.cleanup();
		}

		// Just to be sure nothing gets left out
		getServer().getScheduler().cancelTasks(this);
	}

	@Override
	public void onEnable()
	{
		commandHandler = new CommandHandler(this);
		conf = new ConfigurationManager(this, getDataFolder());
		// Set up the event listeners
		eventManagers.add(new MovingCheckListener(this));
		eventManagers.add(new WorkaroundsListener());
		eventManagers.add(new ChatCheckListener(this));
		eventManagers.add(new BlockBreakCheckListener(this));
		eventManagers.add(new BlockPlaceCheckListener(this));
		eventManagers.add(new FightCheckListener(this));
		eventManagers.add(new InventoryCheckListener(this));

		// Then set up a task to monitor server lag
		if (lagMeasureTask == null)
		{
			lagMeasureTask = new LagMeasureTask(this);
			lagMeasureTask.start();
		}

		// Then print a list of active checks per world
		ActiveCheckPrinter.printActiveChecks(this, eventManagers);

		// register all listeners
		for (EventManager eventManager : eventManagers)
		{
			Bukkit.getPluginManager().registerEvents(eventManager, this);
		}

		getServer().getPluginManager().registerEvents(this, this);
	}

	public ConfigurationCacheStore getConfig(Player player)
	{
		if (player != null)
		{
			return getConfig(player.getWorld());
		}
		else
		{
			return conf.getConfigurationCacheForWorld(null);
		}
	}

	public ConfigurationCacheStore getConfig(World world)
	{
		if (world != null)
		{
			return conf.getConfigurationCacheForWorld(world.getName());
		}
		else
		{
			return conf.getConfigurationCacheForWorld(null);
		}
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		boolean result = commandHandler.handleCommand(this, sender, command, label, args);

		return result;
	}

	public boolean skipCheck()
	{
		if (lagMeasureTask != null)
		{
			return lagMeasureTask.skipCheck();
		}
		return false;
	}

	public void reloadConfiguration()
	{
		conf.cleanup();
		this.conf = new ConfigurationManager(this, this.getDataFolder());
		players.cleanDataMap();
	}

	/**
	 * Call this periodically to walk over the stored data map and remove old/unused entries
	 *
	 */
	public void cleanDataMap()
	{
		players.cleanDataMap();
	}

	/**
	 * An interface method usable by other plugins to collect information about a player. It will include the plugin
	 * version, two timestamps (beginning and end of data collection for that player), and various data from checks)
	 *
	 * @param playerName a player name
	 * @return A newly created map of identifiers and corresponding values
	 */
	public Map<String, Object> getPlayerData(String playerName)
	{

		Map<String, Object> map = players.getPlayerData(playerName);
		map.put("nocheat.version", this.getDescription().getVersion());
		return map;
	}

	public NoCheatPlayer getPlayer(Player player)
	{
		return players.getPlayer(player);
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void logEvent(NoCheatLogEvent event)
	{
		if (event.toConsole())
		{
			// Console logs are not colored
			getServer().getLogger().info(Colors.removeColors(event.getPrefix() + event.getMessage()));
		}
		if (event.toChat())
		{
			for (Player player : Bukkit.getServer().getOnlinePlayers())
			{
				if (player.hasPermission(Permissions.ADMIN_CHATLOG))
				{
					// Chat logs are potentially colored
					player.sendMessage(Colors.replaceColors(event.getPrefix() + event.getMessage()));
				}
			}
		}
		if (event.toFile())
		{
			// File logs are not colored
			fileLogger.info(Colors.removeColors(event.getMessage()));
		}
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event)
	{
		Player p = event.getPlayer();
		String message = "";
		if (!p.hasPermission(Permissions.ZOMBES_FLY))
		{
			message += "§f §f §1 §0 §2 §4"; // Zombes fly
		}
		if (!p.hasPermission(Permissions.ZOMBES_CHEAT))
		{
			message += "§f §f §2 §0 §4 §8"; // Zombes cheat
		}
		if (!p.hasPermission(Permissions.CJB_FLY))
		{
			message += "§3 §9 §2 §0 §0 §1"; // CJB fly
		}
		if (!p.hasPermission(Permissions.CJB_XRAY))
		{
			message += "§3 §9 §2 §0 §0 §2"; // CJB xray
		}
		if (!p.hasPermission(Permissions.CJB_MINIMAP))
		{
			message += "§3 §9 §2 §0 §0 §3"; // CJB minimap players
		}
		p.sendMessage(message);
		System.out.println(message);
	}

	public void setFileLogger(Logger logger)
	{
		this.fileLogger = logger;
	}
}
