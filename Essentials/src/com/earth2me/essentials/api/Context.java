package com.earth2me.essentials.api;

import com.earth2me.essentials.Util;
import com.earth2me.essentials.components.backup.IBackupComponent;
import com.earth2me.essentials.components.economy.IEconomyComponent;
import com.earth2me.essentials.components.items.IItemsComponent;
import com.earth2me.essentials.components.settings.jails.IJailsComponent;
import com.earth2me.essentials.components.settings.kits.IKitsComponent;
import com.earth2me.essentials.components.settings.worths.IWorthsComponent;
import com.earth2me.essentials.components.users.IUserComponent;
import com.earth2me.essentials.components.users.IUsersComponent;
import com.earth2me.essentials.components.warps.IWarpsComponent;
import com.earth2me.essentials.register.payment.PaymentMethods;
import java.io.File;
import java.util.logging.Logger;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Player;


/**
 * Provides a concrete context for Essentials components.
 */
public final class Context implements IContext
{
	@Getter
	@Setter
	private transient IBackupComponent backup;
	@Getter
	@Setter
	private transient IItemsComponent items;
	@Getter
	@Setter
	private transient IGroupsComponent groups;
	@Getter
	@Setter
	private transient IJailsComponent jails;
	@Getter
	@Setter
	private transient IKitsComponent kits;
	@Getter
	@Setter
	private transient ISettingsComponent settings;
	@Getter
	@Setter
	private transient IWarpsComponent warps;
	@Getter
	@Setter
	private transient IWorthsComponent worths;
	@Getter
	@Setter
	private transient PaymentMethods paymentMethods;
	@Getter
	@Setter
	private transient IUsersComponent users;
	@Getter
	@Setter
	private transient II18nComponent i18n;
	@Getter
	@Setter
	private transient ICommandsComponent commands;
	@Getter
	@Setter
	private transient IEconomyComponent economy;
	@Getter
	@Setter
	private transient Server server;
	@Getter
	@Setter
	private transient File dataFolder;
	@SuppressWarnings("NonConstantLogger")
	@Getter
	@Setter
	private transient Logger logger;
	@Getter
	@Setter
	private transient IScheduler scheduler;
	@Getter
	@Setter
	private transient IMessagerComponent messager;
	@Getter
	private transient final IEssentials essentials;

	public Context(final IEssentials essentials)
	{
		this.essentials = essentials;
		setDataFolder(essentials.getDataFolder());
	}

	@Override
	public World getWorld(final String name)
	{
		// Catch invalid parameters.
		if (name == null || name.isEmpty())
		{
			return null;
		}

		if (Util.isNumber(name))
		{
			final int worldId = Integer.parseInt(name);
			if (worldId < getServer().getWorlds().size())
			{
				// Valid number; get world by number.
				return getServer().getWorlds().get(worldId);
			}
		}

		// Otherwise, return by name.
		return getServer().getWorld(name);
	}

	@Override
	public IUserComponent getUser(final Player player)
	{
		return getUsers().getUser(player);
	}

	@Override
	public IUserComponent getUser(final String playerName)
	{
		return getUsers().getUser(playerName);
	}
}
