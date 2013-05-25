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
	 *
	 * @param sender
	 * @param message
	 * @return
	 */
	int broadcastMessage(IUser sender, String message);

	/**
	 *
	 * @return
	 */
	II18n getI18n();

	/**
	 *
	 * @return
	 */
	ISettings getSettings();

	/**
	 *
	 * @return
	 */
	IRanks getRanks();

	/**
	 *
	 * @return
	 */
	IJails getJails();

	/**
	 *
	 * @return
	 */
	IKits getKits();

	/**
	 *
	 * @return
	 */
	IWarps getWarps();

	/**
	 *
	 * @return
	 */
	IWorth getWorth();

	/**
	 *
	 * @return
	 */
	IItemDb getItemDb();

	/**
	 *
	 * @return
	 */
	IUserMap getUserMap();

	/**
	 *
	 * @return
	 */
	IBackup getBackup();

	/**
	 *
	 * @return
	 */
	ICommandHandler getCommandHandler();

	/**
	 *
	 * @param name
	 * @return
	 */
	World getWorld(String name);

	/**
	 *
	 * @return
	 */
	Methods getPaymentMethod();

	/**
	 *
	 * @param groups
	 */
	void setRanks(IRanks groups);

	/**
	 *
	 * @param groups
	 */
	void removeReloadListener(IReload groups);

	/**
	 *
	 * @return
	 */
	IEconomy getEconomy();

	/**
	 *
	 * @return
	 */
	Server getServer();

	/**
	 *
	 * @return
	 */
	Logger getLogger();

	/**
	 *
	 * @return
	 */
	IPlugin getPlugin();

	/**
	 *
	 * @return
	 */
	List<String> getVanishedPlayers();

	/**
	 *
	 * @return
	 */
	EssentialsTimer getTimer();

	/**
	 *
	 * @return
	 */
	Metrics getMetrics();

	/**
	 *
	 * @param metrics
	 */
	void setMetrics(Metrics metrics);

	/**
	 *
	 * @return
	 */
	SpawnsHolder getSpawns();

	/**
	 *
	 * @return
	 */
	StorageQueue getStorageQueue();
}
