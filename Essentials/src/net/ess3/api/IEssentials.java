package net.ess3.api;

import java.util.List;
import java.util.logging.Logger;
import net.ess3.EssentialsTimer;
import net.ess3.economy.register.Methods;
import net.ess3.metrics.Metrics;
import net.ess3.settings.SpawnsHolder;
import net.ess3.storage.StorageQueue;
import org.bukkit.Server;
import org.bukkit.World;


public interface IEssentials extends IComponent
{
	/**
	 *
	 * @param listener
	 */
	void addReloadListener(IReload listener);

	/**
	 * Used to send a message to one or more users
	 *
	 * @param sender the user sending the message
	 * @param message the message to send
	 * @return the number users the message was sent to
	 */
	int broadcastMessage(IUser sender, String message);

	/**
	 * Used to get the i18n interface
	 *
	 * @return the interface
	 */
	II18n getI18n();

	/**
	 * Used to get the Settings interface
	 *
	 * @return the interface
	 */
	ISettings getSettings();

	/**
	 * Used to get the Ranks interface
	 *
	 * @return the interface
	 */
	IRanks getRanks();

	/**
	 * Used to get the Jails interface
	 *
	 * @return the interface
	 */
	IJails getJails();

	/**
	 * Used to get the Kits interface
	 *
	 * @return the interface
	 */
	IKits getKits();

	/**
	 * Used to get the Warps interface
	 *
	 * @return the interface
	 */
	IWarps getWarps();

	/**
	 * Used to get the Worth interface
	 *
	 * @return the interface
	 */
	IWorth getWorth();

	/**
	 * Used to get the ItemDb interface
	 *
	 * @return the interface
	 */
	IItemDb getItemDb();

	/**
	 * Used to get the UserMap interface
	 *
	 * @return the interface
	 */
	IUserMap getUserMap();

	/**
	 * Used to get the Backup interface
	 *
	 * @return the interface
	 */
	IBackup getBackup();

	/**
	 * Used to get the CommandHandler interface
	 *
	 * @return the interface
	 */
	ICommandHandler getCommandHandler();

	/**
	 * Used to get a world with the given name
	 *
	 * @param name the name of the world
	 * @return the world
	 */
	World getWorld(String name);

	/**
	 * Used to get the payment method being used, such as Vault or Boseconomy
	 *
	 * @return the current payment method
	 */
	Methods getPaymentMethod();

	/**
	 *
	 *
	 * @param groups
	 */
	void setRanks(IRanks groups);

	/**
	 *
	 *
	 * @param groups
	 */
	void removeReloadListener(IReload groups);

	/**
	 * Used to get the Economy interface
	 *
	 * @return the interface
	 */
	IEconomy getEconomy();

	/**
	 * Used to get the Server instance
	 *
	 * @return the instance of Server
	 */
	Server getServer();

	/**
	 * Used to get the Logger instance
	 *
	 * @return the instance of Logger
	 */
	Logger getLogger();

	/**
	 * Used to get the Plugin Interface
	 *
	 * @return the interface
	 */
	IPlugin getPlugin();

	/**
	 * Used to get a List of vanished players by name
	 *
	 * @return a List of vanished players
	 */
	List<String> getVanishedPlayers();

	/**
	 * Used to get the timer
	 *
	 * @return the instance of EssentialsTimer
	 */
	EssentialsTimer getTimer();

	/**
	 * Used internally. **maybe we should make this private**
	 *
	 * @return
	 */
	Metrics getMetrics();

	/**
	 * Used internally. **maybe we should make this private**
	 *
	 * @param metrics
	 */
	void setMetrics(Metrics metrics);

	/**
	 * Used to get the instance of SpawnsHolder
	 *
	 * @return the instance
	 */
	SpawnsHolder getSpawns();

	/**
	 * Used to get the instance of StorageQueue
	 *
	 * @return the instance
	 */
	StorageQueue getStorageQueue();
}
