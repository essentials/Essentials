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
package com.earth2me.essentials;

import static com.earth2me.essentials.I18n._;
import com.earth2me.essentials.api.*;
import com.earth2me.essentials.components.ComponentPlugin;
import com.earth2me.essentials.components.commands.CommandsComponent;
import com.earth2me.essentials.components.economy.Economy;
import com.earth2me.essentials.components.economy.IEconomyComponent;
import com.earth2me.essentials.components.economy.IWorthsComponent;
import com.earth2me.essentials.components.economy.WorthsComponent;
import com.earth2me.essentials.components.items.ItemsComponent;
import com.earth2me.essentials.components.jails.IJailsComponent;
import com.earth2me.essentials.components.jails.JailsComponent;
import com.earth2me.essentials.components.kits.IKitsComponent;
import com.earth2me.essentials.components.kits.KitsComponent;
import com.earth2me.essentials.components.users.IUser;
import com.earth2me.essentials.components.users.IUsersComponent;
import com.earth2me.essentials.components.users.UsersComponent;
import com.earth2me.essentials.components.warps.IWarpsComponent;
import com.earth2me.essentials.components.warps.WarpsComponent;
import com.earth2me.essentials.listener.*;
import com.earth2me.essentials.settings.GroupsComponent;
import com.earth2me.essentials.settings.SettingsHolder;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.yaml.snakeyaml.error.YAMLException;


public class Essentials extends ComponentPlugin implements IEssentials
{
	private static final Logger logger = Logger.getLogger("Minecraft");
	
	public transient boolean testing;
	
	private transient final TntExplodeListener tntListener;
	private transient ExecuteTimer execTimer;
	private transient final Context context;

	public Essentials()
	{
		context = new Context();
		tntListener = new TntExplodeListener(context);
	}

	@Override
	public Context getContext()
	{
		return context;
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
		i18n = new I18n(this);
		i18n.onEnable();
		logger.log(Level.INFO, _("usingTempFolderForTesting"));
		logger.log(Level.INFO, dataFolder.toString());
		this.initialize(null, server, new PluginDescriptionFile(new FileReader(new File("src" + File.separator + "plugin.yml"))), dataFolder, null, null);
		settings = new SettingsHolder(this);
		i18n.updateLocale("en");
		users = new UsersComponent(this);
		//permissionsHandler = new PermissionsHandler(this);
		economy = new Economy(this);
	}

	private boolean checkVersion()
	{
		try
		{
			if (!new Versioning().checkServerVersion(getServer(), getDescription().getVersion()))
			{
				setEnabled(false);
				return false;
			}
			else
			{
				return true;
			}
		}
		finally
		{
			execTimer.mark("BukkitCheck");
		}
	}

	@Override
	public void onEnable()
	{
		execTimer = new ExecuteTimer();
		execTimer.start();

		registerComponents(0);

		if (!checkVersion())
		{
			return;
		}

		try
		{
			registerComponents(1);
			
			i18n.updateLocale(settings.getLocale());
			
			registerComponents(2);
			
			reload();
		}
		catch (YAMLException exception)
		{
			if (pluginManager.getPlugin("EssentialsUpdate") != null)
			{
				logger.log(Level.SEVERE, _("essentialsHelp2"));
			}
			else
			{
				logger.log(Level.SEVERE, _("essentialsHelp1"));
			}
			logger.log(Level.SEVERE, exception.toString());
			pluginManager.registerEvents(new Listener()
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
		backup = new Backup(this);
		
		final PluginManager pluginManager = getServer().getPluginManager();
		registerNormalListeners(pluginManager);
		
		registerComponents(LAST);

		registerLateListeners(pluginManager);


		final EssentialsTimer timer = new EssentialsTimer(this);
		getServer().getScheduler().scheduleSyncRepeatingTask(this, timer, 1, 100);
		execTimer.mark("RegListeners");
		final String timeroutput = execTimer.end();
		if (getSettings().isDebug())
		{
			logger.log(Level.INFO, "Essentials load {0}", timeroutput);
		}
	}
	
	private void registerComponents(int stage)
	{
		switch (stage)
		{
		case 0:
			context.setI18n(new I18n(context));
			add(context.getI18n());
			execTimer.mark("I18n1");
			break;
			
		case 1:
			settings = new SettingsHolder(this);
			reloadList.add(settings);
			execTimer.mark("Settings");
			break;
			
		case 2:
			final IUsersComponent users = new UsersComponent(context);
			add(users);
			execTimer.mark("Init(Usermap)");
			
			final GroupsComponent groups = new GroupsComponent(context);
			add((GroupsComponent)groups);
			
			final IWarpsComponent warps = new WarpsComponent(context);
			add(warps);
			execTimer.mark("Init(Spawn/Warp)");
			
			final IWorthsComponent worths = new WorthsComponent(context);
			add(worths);
			
			final IItemsComponent items = new ItemsComponent(context);
			add(items);
			execTimer.mark("Init(Worth/ItemDB)");
			
			final IKitsComponent kits = new KitsComponent(context);
			add(kits);
			
			final ICommandsComponent commands = new CommandsComponent(Essentials.class.getClassLoader(), "com.earth2me.essentials.components.commands.handlers.Command", "essentials.", context);
			add(commands);
			
			final IEconomyComponent economy = new Economy(context);
			add(economy);
			break;
			
		default:
			final IJailsComponent jails = new JailsComponent(context);
			context.setJails(jails);
			add(jails);
			break;
		}
	}
	
	private void registerNormalListeners(PluginManager pluginManager)
	{
		pluginManager.registerEvents(new EssentialsPluginListener(context), this);
		pluginManager.registerEvents(new EssentialsPlayerListener(context), this);
		pluginManager.registerEvents(new EssentialsBlockListener(context), this);
		pluginManager.registerEvents(new EssentialsEntityListener(context), this);
	}
	
	private void registerLateListeners(PluginManager pluginManager)
	{
		pluginManager.registerEvents(tntListener, this);
	}

	@Override
	public void onDisable()
	{
		i18n.onDisable();
		Trade.closeLog();
	}

	@Override
	public void reload()
	{
		Trade.closeLog();

		for (IReloadable iReload : reloadList)
		{
			iReload.onReload();
			execTimer.mark("Reload(" + iReload.getClass().getSimpleName() + ")");
		}

		i18n.updateLocale(settings.getLocale());
	}

	@Override
	public boolean onCommand(final CommandSender sender, final Command command, final String commandLabel, final String[] args)
	{
		return commandHandler.handleCommand(sender, command, commandLabel, args);
		//return onCommandEssentials(sender, command, commandLabel, args, Essentials.class.getClassLoader(), "com.earth2me.essentials.commands.Command", "essentials.", null);
	}

	@Override
	public void addReloadListener(final IReloadable listener)
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
		if (sender.isHidden())
		{
			return 0;
		}
		final Player[] players = getServer().getOnlinePlayers();

		for (Player player : players)
		{
			final IUser user = getUser(player);
			if (!user.isIgnoringPlayer(sender.getName()))
			{
				player.sendMessage(message);
			}
		}

		return players.length;
	}

	@Override
	public int scheduleAsyncDelayedTask(final Runnable run)
	{
		return this.getServer().getScheduler().scheduleAsyncDelayedTask(this, run);
	}

	@Override
	public int scheduleSyncDelayedTask(final Runnable run)
	{
		return this.getServer().getScheduler().scheduleSyncDelayedTask(this, run);
	}

	@Override
	public int scheduleSyncDelayedTask(final Runnable run, final long delay)
	{
		return this.getServer().getScheduler().scheduleSyncDelayedTask(this, run, delay);
	}

	@Override
	public int scheduleSyncRepeatingTask(final Runnable run, final long delay, final long period)
	{
		return this.getServer().getScheduler().scheduleSyncRepeatingTask(this, run, delay, period);
	}

	@Override
	public TntExplodeListener getTntListener()
	{
		return tntListener;
	}

	/*
	 * @Override public PermissionsHandler getPermissionsHandler() { return permissionsHandler;
	}
	 */
	@Override
	public void setGroups(final IGroupsComponent groups)
	{
		this.groups = groups;
	}

	@Override
	public void removeReloadListener(IReloadable groups)
	{
		this.reloadList.remove(groups);
	}
}
