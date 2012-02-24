package com.earth2me.essentials.api;

import com.earth2me.essentials.I18n;
import com.earth2me.essentials.components.economy.IEconomyComponent;
import com.earth2me.essentials.components.economy.IWorthsComponent;
import com.earth2me.essentials.components.jails.IJailsComponent;
import com.earth2me.essentials.components.kits.IKitsComponent;
import com.earth2me.essentials.components.users.IUser;
import com.earth2me.essentials.components.users.IUsersComponent;
import com.earth2me.essentials.components.warps.IWarpsComponent;
import com.earth2me.essentials.register.payment.PaymentMethods;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Player;


/**
 * Provides a concrete context for Essentials components.
 */
public final class Context implements IContext
{
	private transient IBackup backup;
	private transient IItemsComponent items;
	private transient IGroupsComponent groups;
	private transient IJailsComponent jails;
	private transient IKitsComponent kits;
	private transient ISettings settings;
	private transient IWarpsComponent warps;
	private transient IWorthsComponent worths;
	private transient PaymentMethods paymentMethods; // TODO = new PaymentMethods();
	private transient IUsersComponent users;
	private transient I18n i18n;
	private transient ICommandsComponent commands;
	private transient IEconomyComponent economy;
	private transient Server server;
	
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
	public IBackup getBackup()
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
	public ISettings getSettings()
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
	public I18n getI18n()
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

	public void setBackup(IBackup backup)
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

	public void setSettings(ISettings settings)
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

	public void setI18n(I18n i18n)
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
}
