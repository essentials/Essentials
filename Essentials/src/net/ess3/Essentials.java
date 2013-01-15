/*
 * Essentials - a bukkit plugin
 * Copyright (C) 2011 - 2012  Essentials Team, http://contributors.ess3.net
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
import lombok.Setter;
import static net.ess3.I18n._;
import net.ess3.api.*;
import net.ess3.backup.Backup;
import net.ess3.commands.EssentialsCommandHandler;
import net.ess3.economy.Economy;
import net.ess3.economy.Trade;
import net.ess3.economy.WorthHolder;
import net.ess3.economy.register.Methods;
import net.ess3.metrics.Metrics;
import net.ess3.ranks.RanksStorage;
import net.ess3.settings.SettingsHolder;
import net.ess3.settings.SpawnsHolder;
import net.ess3.storage.StorageQueue;
import net.ess3.user.UserMap;
import net.ess3.utils.ExecuteTimer;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.InvalidDescriptionException;


public class Essentials implements IEssentials
{
	@Getter
	private ISettings settings;
	@Getter
	private IJails jails;
	@Getter
	private IKits kits;
	@Getter
	private IWarps warps;
	@Getter
	private IWorth worth;
	@Getter
	private IBackup backup;
	@Getter
	private IItemDb itemDb;
	@Getter
	@Setter
	private IRanks ranks;
	@Getter
	private SpawnsHolder spawns;
	@Getter
	private final Methods paymentMethod = new Methods();
	@Getter
	private IUserMap userMap;
	@Getter
	private final I18n i18n;
	@Getter
	private ICommandHandler commandHandler;
	@Getter
	private Economy economy;
	@Getter
	private final Server server;
	@Getter
	private final Logger logger;
	@Getter
	private final IPlugin plugin;
	@Getter
	@Setter
	private Metrics metrics;
	@Getter
	private EssentialsTimer timer;
	@Getter
	private List<String> vanishedPlayers = new ArrayList<String>();
	@Getter
	private final StorageQueue storageQueue;
	private ExecuteTimer execTimer;
	public static boolean testing;
	private List<IReload> reloadList;

	public Essentials(final Server server, final Logger logger, final IPlugin plugin)
	{
		this.server = server;
		this.logger = logger;
		this.plugin = plugin;
		this.storageQueue = new StorageQueue(plugin);
		this.i18n = new I18n(this);
		i18n.onEnable();
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

		logger.log(Level.INFO, _("usingTempFolderForTesting"));
		logger.log(Level.INFO, dataFolder.toString());
		storageQueue.setEnabled(true);
		settings = new SettingsHolder(this);
		i18n.updateLocale("en");
		userMap = new UserMap(this);
		economy = new Economy(this);
	}

	@Override
	public void onEnable()
	{
		storageQueue.setEnabled(true);
		execTimer = new ExecuteTimer();
		execTimer.start();

		execTimer.mark("I18n1");

		execTimer.mark("BukkitCheck");

		reloadList = new ArrayList<IReload>();
		settings = new SettingsHolder(this);
		reloadList.add(settings);
		execTimer.mark("Settings");
		i18n.updateLocale(settings.getLocale());
		userMap = new UserMap(this);
		reloadList.add(userMap);
		execTimer.mark("Init(Usermap)");
		ranks = new RanksStorage(this);
		reloadList.add((RanksStorage)ranks);
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

		backup = new Backup(this);

		jails = new Jails(this);
		reloadList.add(jails);


		timer = new EssentialsTimer(this);
		getPlugin().scheduleSyncRepeatingTask(timer, 1, 100);
		execTimer.mark("RegListeners");

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
			IUser user = getUserMap().getUser(p);
			if (user.isVanished())
			{
				user.toggleVanished();
				p.sendMessage(_("unvanishedReload"));
			}
		}
		i18n.onDisable();
		Trade.closeLog();
		storageQueue.setEnabled(false);
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
	public int broadcastMessage(final IUser sender, final String message)
	{
		if (sender == null)
		{
			return getServer().broadcastMessage(message);
		}
		Player userPlayer = sender.getPlayer();
		int delivered = 0;
		for (Player player : getServer().getOnlinePlayers())
		{
			final IUser user = getUserMap().getUser(player);
			if (!user.isIgnoringPlayer(sender) && player.canSee(userPlayer))
			{
				player.sendMessage(message);
				delivered++;
			}
		}

		return delivered;
	}

	@Override
	public void removeReloadListener(IReload groups)
	{
		this.reloadList.remove(groups);
	}
}
