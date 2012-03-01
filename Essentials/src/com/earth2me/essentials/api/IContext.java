package com.earth2me.essentials.api;

import com.earth2me.essentials.components.backup.IBackupComponent;
import com.earth2me.essentials.components.economy.IEconomyComponent;
import com.earth2me.essentials.components.settings.jails.IJailsComponent;
import com.earth2me.essentials.components.settings.kits.IKitsComponent;
import com.earth2me.essentials.components.settings.worths.IWorthsComponent;
import com.earth2me.essentials.components.users.IUserComponent;
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
	IEssentials getEssentials();

	IMessagerComponent getMessager();

	Logger getLogger();

	IScheduler getScheduler();

	File getDataFolder();

	IUserComponent getUser(Player player);

	IUserComponent getUser(String playerName);

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
