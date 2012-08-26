/*
 * Essentials - a bukkit plugin
 * Copyright (C) 2011  Essentials Team
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.ess3;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.Getter;
import static net.ess3.I18n._;
import net.ess3.api.*;
import net.ess3.backup.Backup;
import net.ess3.commands.EssentialsCommandHandler;
import net.ess3.economy.Economy;
import net.ess3.economy.Trade;
import net.ess3.economy.WorthHolder;
import net.ess3.economy.register.Methods;
import net.ess3.listener.*;
import net.ess3.metrics.Metrics;
import net.ess3.metrics.MetricsListener;
import net.ess3.metrics.MetricsStarter;
import net.ess3.ranks.RanksStorage;
import net.ess3.settings.SettingsHolder;
import net.ess3.settings.SpawnsHolder;
import net.ess3.user.UserMap;
import net.ess3.utils.ExecuteTimer;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.Plugin;
import org.yaml.snakeyaml.error.YAMLException;


public class Essentials implements IEssentials
{
	private transient ISettings settings;
	private transient IJails jails;
	private transient IKits kits;
	private transient IWarps warps;
	private transient IWorth worth;
	private transient List<IReload> reloadList;
	private transient IBackup backup;
	private transient IItemDb itemDb;
	private transient IRanks groups;
	private transient SpawnsHolder spawns;
	private transient final Methods paymentMethod = new Methods();
	//private transient PermissionsHandler permissionsHandler;
	private transient IUserMap userMap;
	private transient ExecuteTimer execTimer;
	@Getter
	private final I18n i18n;
	private transient ICommandHandler commandHandler;
	private transient Economy economy;
	@Getter
	private final Server server;
	@Getter
	private final Logger logger;
	@Getter
	private final IPlugin plugin;
	public static boolean testing;
	private transient Metrics metrics;
	@Getter
	private transient EssentialsTimer timer;
	@Getter
	private transient List<String> vanishedPlayers = new ArrayList<String>();

	public Essentials(final Server server, final Logger logger, final Plugin plugin)
	{
		this.server = server;
		this.logger = logger;
		this.plugin = plugin;
		this.i18n = new I18n(this);
		i18n.onEnable();
	}

	@Override
	public ISettings getSettings()
	{
		return settings;
	}

	public void setupForTesting(final Server server) throws IOException, InvalidDescriptionException
	{
		testing = true;
		final File dataFolder = File.createTempFile("essentialstest", "");
		if (!dataFolder.delete())
		{
			throw new IOException();
		}
		if (!dataFolder.mkdir())
		{
			throw new IOException();
		}

		logger.log(Level.INFO, I18n._("usingTempFolderForTesting"));
		logger.log(Level.INFO, dataFolder.toString());
		//this.initialize(null, server, new PluginDescriptionFile(new FileReader(new File("src" + File.separator + "plugin.yml"))), dataFolder, null, null);
		settings = new SettingsHolder(this);
		i18n.updateLocale("en");
		userMap = new UserMap(this);
		//permissionsHandler = new PermissionsHandler(this);
		economy = new Economy(this);
	}

	@Override
	public void onEnable()
	{
		execTimer = new ExecuteTimer();
		execTimer.start();

		execTimer.mark("I18n1");
		
		execTimer.mark("BukkitCheck");
		try
		{
			//final EssentialsUpgrade upgrade = new EssentialsUpgrade(this);
			//upgrade.beforeSettings();
			//execTimer.mark("Upgrade");
			reloadList = new ArrayList<IReload>();
			settings = new SettingsHolder(this);
			reloadList.add(settings);
			execTimer.mark("Settings");
			//upgrade.afterSettings();
			//execTimer.mark("Upgrade2");
			i18n.updateLocale(settings.getLocale());
			userMap = new UserMap(this);
			reloadList.add(userMap);
			execTimer.mark("Init(Usermap)");
			groups = new RanksStorage(this);
			reloadList.add((RanksStorage)groups);
			warps = new Warps(this);
			reloadList.add(warps);
			execTimer.mark("Init(Spawn/Warp)");
			worth = new WorthHolder(this);
			reloadList.add(worth);
			itemDb = new ItemDb(this);
			reloadList.add(itemDb);
			execTimer.mark("Init(Worth/ItemDB)");
			kits = new Kits(this);
			reloadList.add(kits);
			commandHandler = new EssentialsCommandHandler(Essentials.class.getClassLoader(), "net.ess3.commands.Command", "essentials.", this);
			reloadList.add(commandHandler);
			economy = new Economy(this);
			reloadList.add(economy);
			spawns = new SpawnsHolder(this);
			reloadList.add(spawns);
			onReload();
		}
		catch (YAMLException exception)
		{
			if (pm.getPlugin("EssentialsUpdate") != null)
			{
				logger.log(Level.SEVERE, I18n._("essentialsHelp2"));
			}
			else
			{
				logger.log(Level.SEVERE, I18n._("essentialsHelp1"));
			}
			logger.log(Level.SEVERE, exception.toString());
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
			this.getPlugin().setEnabled(false);
			return;
		}
		backup = new Backup(this);
		//permissionsHandler = new PermissionsHandler(this);
		final EssentialsPluginListener serverListener = new EssentialsPluginListener(this);
		pm.registerEvents(serverListener, this);
		reloadList.add(serverListener);

		final EssentialsPlayerListener playerListener = new EssentialsPlayerListener(this);
		pm.registerEvents(playerListener, this);

		final EssentialsBlockListener blockListener = new EssentialsBlockListener(this);
		pm.registerEvents(blockListener, this);

		final EssentialsEntityListener entityListener = new EssentialsEntityListener(this);
		pm.registerEvents(entityListener, this);

		jails = new Jails(this);
		reloadList.add(jails);


		timer = new EssentialsTimer(this);
		getPlugin().scheduleSyncRepeatingTask(timer, 1, 100);
		execTimer.mark("RegListeners");

		final MetricsStarter metricsStarter = new MetricsStarter(this);
		if (metricsStarter.getStart() != null && metricsStarter.getStart() == true)
		{
			getServer().getScheduler().scheduleAsyncDelayedTask(this.getPlugin(), metricsStarter, 1);
		}
		else if (metricsStarter.getStart() != null && metricsStarter.getStart() == false)
		{
			final MetricsListener metricsListener = new MetricsListener(this, metricsStarter);
			pm.registerEvents(metricsListener, this);
		}

		final String timeroutput = execTimer.end();
		if (getSettings().isDebug())
		{
			logger.log(Level.INFO, "Essentials load {0}", timeroutput);
		}
	}

	@Override
	public void onDisable()
	{
		for (Player p : getServer().getOnlinePlayers())
		{
			IUser user = getUser(p);
			if (user.isVanished())
			{
				user.toggleVanished();
				p.sendMessage(_("unvanishedReload"));
			}
		}
		i18n.onDisable();
		Trade.closeLog();
	}

	@Override
	public void onReload()
	{
		Trade.closeLog();

		for (IReload iReload : reloadList)
		{
			iReload.onReload();
			execTimer.mark("Reload(" + iReload.getClass().getSimpleName() + ")");
		}

		i18n.updateLocale(settings.getLocale());
	}

	@Override
	public IJails getJails()
	{
		return jails;
	}

	@Override
	public IKits getKits()
	{
		return kits;
	}

	@Override
	public IWarps getWarps()
	{
		return warps;
	}

	@Override
	public IWorth getWorth()
	{
		return worth;
	}

	@Override
	public IBackup getBackup()
	{
		return backup;
	}

	@Override
	public Metrics getMetrics()
	{
		return metrics;
	}

	@Override
	public void setMetrics(Metrics metrics)
	{
		this.metrics = metrics;
	}

	@Override
	public World getWorld(final String name)
	{
		if (name.matches("[0-9]+"))
		{
			final int worldId = Integer.parseInt(name);
			if (worldId < getServer().getWorlds().size())
			{
				return getServer().getWorlds().get(worldId);
			}
		}
		return getServer().getWorld(name);
	}

	@Override
	public void addReloadListener(final IReload listener)
	{
		reloadList.add(listener);
	}

	@Override
	public Methods getPaymentMethod()
	{
		return paymentMethod;
	}

	@Override
	public int broadcastMessage(final IUser sender, final String message)
	{
		if (sender == null)
		{
			return getServer().broadcastMessage(message);
		}
		if (sender.isHidden())
		{
			return 0;
		}
		for (Player player : getServer().getOnlinePlayers())
		{
			final IUser user = player.getUser();
			if (!user.isIgnoringPlayer(sender))
			{
				player.sendMessage(message);
			}
		}

		return getServer().getOnlinePlayers().length;
	}

	/*
	 * @Override public PermissionsHandler getPermissionsHandler() { return permissionsHandler; }
	 */
	@Override
	public IItemDb getItemDb()
	{
		return itemDb;
	}

	@Override
	public IUserMap getUserMap()
	{
		return userMap;
	}
	
	@Override
	public IRanks getRanks()
	{
		return groups;
	}

	@Override
	public ICommandHandler getCommandHandler()
	{
		return commandHandler;
	}

	@Override
	public void setRanks(final IRanks groups)
	{
		this.groups = groups;
	}

	@Override
	public void removeReloadListener(IReload groups)
	{
		this.reloadList.remove(groups);
	}

	@Override
	public IEconomy getEconomy()
	{
		return economy;
	}
}
