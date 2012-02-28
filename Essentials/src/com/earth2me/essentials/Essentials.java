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

import com.earth2me.essentials.api.*;
import com.earth2me.essentials.components.ComponentPlugin;
import com.earth2me.essentials.components.backup.BackupComponent;
import com.earth2me.essentials.components.commands.CommandsComponent;
import com.earth2me.essentials.components.economy.EconomyComponent;
import com.earth2me.essentials.components.economy.IEconomyComponent;
import com.earth2me.essentials.components.i18n.I18nComponent;
import static com.earth2me.essentials.components.i18n.I18nComponent._;
import com.earth2me.essentials.components.items.ItemsComponent;
import com.earth2me.essentials.components.jails.IJailsComponent;
import com.earth2me.essentials.components.jails.JailsComponent;
import com.earth2me.essentials.components.kits.IKitsComponent;
import com.earth2me.essentials.components.kits.KitsComponent;
import com.earth2me.essentials.components.settings.SettingsComponent;
import com.earth2me.essentials.components.settings.economy.IWorthsComponent;
import com.earth2me.essentials.components.settings.economy.WorthsComponent;
import com.earth2me.essentials.components.settings.groups.GroupsComponent;
import com.earth2me.essentials.components.settings.warps.IWarpsComponent;
import com.earth2me.essentials.components.settings.warps.WarpsComponent;
import com.earth2me.essentials.components.settings.users.IUsersComponent;
import com.earth2me.essentials.components.settings.users.UsersComponent;
import com.earth2me.essentials.listeners.*;
import com.earth2me.essentials.register.payment.PaymentMethods;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
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
	private transient boolean testing;
	private transient final TntExplodeListener tntListener;
	private transient ExecuteTimer execTimer;
	private transient final Context context;

	public Essentials()
	{
		context = new Context(this);
		context.setPaymentMethods(new PaymentMethods());

		tntListener = new TntExplodeListener(context);
	}

	@Override
	public Context getContext()
	{
		return context;
	}

	public void setupForTesting(final Server server) throws IOException, InvalidDescriptionException
	{
		setTesting(true);

		execTimer = new ExecuteTimer();
		execTimer.start();

		final File dataFolder = File.createTempFile("essentialstest", "");
		if (!dataFolder.delete())
		{
			throw new IOException();
		}
		if (!dataFolder.mkdir())
		{
			throw new IOException();
		}

		registerComponents(0);

		logger.log(Level.INFO, _("usingTempFolderForTesting"));
		logger.log(Level.INFO, dataFolder.toString());
		this.initialize(null, server, new PluginDescriptionFile(new FileReader(new File("src" + File.separator + "plugin.yml"))), dataFolder, null, null);

		registerComponents(1);

		context.getI18n().updateLocale("en");

		registerComponents(2);
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
			context.getI18n().reload();
			registerComponents(2);

			reload();
		}
		catch (YAMLException exception)
		{
			onLoadException(exception);
			return;
		}

		registerComponents(3);

		final PluginManager pluginManager = getServer().getPluginManager();
		registerNormalListeners(pluginManager);

		registerComponents(4);

		registerLateListeners(pluginManager);


		final EssentialsTimer timer = new EssentialsTimer(context);
		getServer().getScheduler().scheduleSyncRepeatingTask(this, timer, 1, 100);
		execTimer.mark("RegListeners");
		final String timeroutput = execTimer.end();
		if (context.getSettings().isDebug())
		{
			logger.log(Level.INFO, "Essentials load {0}", timeroutput);
		}
	}

	private void onLoadException(Exception exception)
	{
		final PluginManager pluginManager = getServer().getPluginManager();

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
	}

	private void registerComponents(int stage)
	{
		switch (stage)
		{
		case 0:
			final II18nComponent i18n = new I18nComponent(context);
			context.setI18n(i18n);
			add(i18n);
			execTimer.mark("I18n1");
			break;

		case 1:
			final ISettingsComponent settings = new SettingsComponent(context, this);
			context.setSettings(settings);
			add(settings);
			execTimer.mark("Settings");
			break;

		case 2:
			final IUsersComponent users = new UsersComponent(context, this);
			context.setUsers(users);
			add(users);
			execTimer.mark("Init(Usermap)");

			final IGroupsComponent groups = new GroupsComponent(context, this);
			context.setGroups(groups);
			add(groups);

			final IWarpsComponent warps = new WarpsComponent(context, this);
			context.setWarps(warps);
			add(warps);
			execTimer.mark("Init(Spawn/Warp)");

			final IWorthsComponent worths = new WorthsComponent(context, this);
			context.setWorths(worths);
			add(worths);

			final IItemsComponent items = new ItemsComponent(context);
			context.setItems(items);
			add(items);
			execTimer.mark("Init(Worth/ItemDB)");

			final IKitsComponent kits = new KitsComponent(context, this);
			context.setKits(kits);
			add(kits);

			final ICommandsComponent commands = new CommandsComponent(Essentials.class.getClassLoader(), "com.earth2me.essentials.components.commands.handlers.Command", "essentials.", context);
			context.setCommands(commands);
			add(commands);

			final IEconomyComponent economy = new EconomyComponent(context);
			context.setEconomy(economy);
			add(economy);
			break;

		case 3:
			final IBackupComponent backup = new BackupComponent(context);
			context.setBackup(backup);
			add(backup);
			break;

		case 4:
			final IJailsComponent jails = new JailsComponent(context, this);
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
		clear();
		Trade.closeLog();
	}

	@Override
	// Override it so we can mark with execTimer.
	public void reload()
	{
		Trade.closeLog();

		for (IReloadable reloadable : this)
		{
			reloadable.reload();
			execTimer.mark("Reload(" + reloadable.getClass().getSimpleName() + ")");
		}
	}

	@Override
	public boolean onCommand(final CommandSender sender, final Command command, final String commandLabel, final String[] args)
	{
		return context.getCommands().handleCommand(sender, command, commandLabel, args);
	}

	@Override
	public TntExplodeListener getTntListener()
	{
		return tntListener;
	}

	public boolean isTesting()
	{
		return testing;
	}

	public void setTesting(boolean testing)
	{
		this.testing = testing;
	}
}
