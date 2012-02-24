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
 * Provides a context for Essentials components.
 */
public interface IContext
{
	Logger getLogger();
	
	IScheduler getScheduler();
	
	File getDataFolder();
	
	IUser getUser(Player player);

	IUser getUser(String playerName);

	II18nComponent getI18n();

	ISettingsComponent getSettings();

	IGroupsComponent getGroups();

	IJailsComponent getJails();

	IKitsComponent getKits();

	IWarpsComponent getWarps();

	IWorthsComponent getWorths();

	IItemsComponent getItems();

	IUsersComponent getUsers();

	IBackupComponent getBackup();

	ICommandsComponent getCommands();

	World getWorld(String name);

	PaymentMethods getPaymentMethods();

	IEconomyComponent getEconomy();
	
	Server getServer();
}
