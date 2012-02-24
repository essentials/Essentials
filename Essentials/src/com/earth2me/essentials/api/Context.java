package com.earth2me.essentials.api;

import com.earth2me.essentials.components.economy.IEconomyComponent;
import com.earth2me.essentials.components.economy.IWorthsComponent;
import com.earth2me.essentials.components.jails.IJailsComponent;
import com.earth2me.essentials.components.kits.IKitsComponent;
import com.earth2me.essentials.components.users.IUser;
import com.earth2me.essentials.components.users.IUsersComponent;
import com.earth2me.essentials.components.warps.IWarpsComponent;
import com.earth2me.essentials.register.payment.PaymentMethods;
import java.io.File;
import java.util.logging.Logger;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Player;


/**
 * Provides a concrete context for Essentials components.
 */
public final class Context implements IContext
{
	private transient IBackupComponent backup;
	private transient IItemsComponent items;
	private transient IGroupsComponent groups;
	private transient IJailsComponent jails;
	private transient IKitsComponent kits;
	private transient ISettingsComponent settings;
	private transient IWarpsComponent warps;
	private transient IWorthsComponent worths;
	private transient PaymentMethods paymentMethods;
	private transient IUsersComponent users;
	private transient II18nComponent i18n;
	private transient ICommandsComponent commands;
	private transient IEconomyComponent economy;
	private transient Server server;
	public transient File dataFolder;
	@SuppressWarnings("NonConstantLogger")
	public transient Logger logger;
	public transient IScheduler scheduler;
	public transient IMessagerComponent messager;

	@Override
	public World getWorld(final String name)
	{
		// Catch invalid parameters.
		if (name == null || name.length() < 1)
		{
			return null;
		}
		
		// Check to see if the name is numeric.
		char[] chars = name.toCharArray();
		for (int i = 0; i < chars.length; i++)
		{
			if (chars[i] < '0' || chars[i] > '1')
			{
				// If not, get the world normally.
				return getServer().getWorld(name);
			}
		}

		final int worldId = Integer.parseInt(name);
		if (worldId < getServer().getWorlds().size())
		{
			// Valid number; get world by number.
			return getServer().getWorlds().get(worldId);
		}
		
		// Otherwise, return by name.
		return getServer().getWorld(name);
	}

	@Override
	public IUser getUser(final Player player)
	{
		return getUsers().getUser(player);
	}

	@Override
	public IUser getUser(final String playerName)
	{
		return getUsers().getUser(playerName);
	}

	@Override
	public File getDataFolder()
	{
		return dataFolder;
	}

	@Override
	public Logger getLogger()
	{
		return logger;
	}

	@Override
	public IScheduler getScheduler()
	{
		return scheduler;
	}

	@Override
	public IBackupComponent getBackup()
	{
		return backup;
	}

	@Override
	public IItemsComponent getItems()
	{
		return items;
	}

	@Override
	public IGroupsComponent getGroups()
	{
		return groups;
	}

	@Override
	public IJailsComponent getJails()
	{
		return jails;
	}

	@Override
	public IKitsComponent getKits()
	{
		return kits;
	}

	@Override
	public ISettingsComponent getSettings()
	{
		return settings;
	}

	@Override
	public IWarpsComponent getWarps()
	{
		return warps;
	}

	@Override
	public IWorthsComponent getWorths()
	{
		return worths;
	}

	@Override
	public PaymentMethods getPaymentMethods()
	{
		return paymentMethods;
	}

	@Override
	public IUsersComponent getUsers()
	{
		return users;
	}

	@Override
	public II18nComponent getI18n()
	{
		return i18n;
	}

	@Override
	public ICommandsComponent getCommands()
	{
		return commands;
	}

	@Override
	public IEconomyComponent getEconomy()
	{
		return economy;
	}

	@Override
	public Server getServer()
	{
		return server;
	}

	@Override
	public IMessagerComponent getMessager()
	{
		return messager;
	}

	public void setBackup(IBackupComponent backup)
	{
		this.backup = backup;
	}

	public void setItems(IItemsComponent items)
	{
		this.items = items;
	}

	public void setGroups(IGroupsComponent groups)
	{
		this.groups = groups;
	}

	public void setJails(IJailsComponent jails)
	{
		this.jails = jails;
	}

	public void setKits(IKitsComponent kits)
	{
		this.kits = kits;
	}

	public void setSettings(ISettingsComponent settings)
	{
		this.settings = settings;
	}

	public void setWarps(IWarpsComponent warps)
	{
		this.warps = warps;
	}

	public void setWorths(IWorthsComponent worths)
	{
		this.worths = worths;
	}

	public void setPaymentMethods(PaymentMethods paymentMethods)
	{
		this.paymentMethods = paymentMethods;
	}

	public void setUsers(IUsersComponent users)
	{
		this.users = users;
	}

	public void setI18n(II18nComponent i18n)
	{
		this.i18n = i18n;
	}

	public void setCommands(ICommandsComponent commands)
	{
		this.commands = commands;
	}

	public void setEconomy(IEconomyComponent economy)
	{
		this.economy = economy;
	}

	public void setServer(Server server)
	{
		this.server = server;
	}

	public void setDataFolder(File dataFolder)
	{
		this.dataFolder = dataFolder;
	}

	public void setLogger(Logger logger)
	{
		this.logger = logger;
	}

	public void setScheduler(IScheduler scheduler)
	{
		this.scheduler = scheduler;
	}

	public void setMessager(IMessagerComponent messager)
	{
		this.messager = messager;
	}
}
