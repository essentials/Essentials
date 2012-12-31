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
	void addReloadListener(IReload listener);

	int broadcastMessage(IUser sender, String message);

	II18n getI18n();

	ISettings getSettings();

	IRanks getRanks();

	IJails getJails();

	IKits getKits();

	IWarps getWarps();

	IWorth getWorth();

	IItemDb getItemDb();

	IUserMap getUserMap();

	IBackup getBackup();

	ICommandHandler getCommandHandler();

	World getWorld(String name);

	Methods getPaymentMethod();

	void setRanks(IRanks groups);

	void removeReloadListener(IReload groups);

	IEconomy getEconomy();

	Server getServer();

	Logger getLogger();

	IPlugin getPlugin();

	List<String> getVanishedPlayers();

	EssentialsTimer getTimer();

	Metrics getMetrics();

	void setMetrics(Metrics metrics);

	SpawnsHolder getSpawns();
	
	StorageQueue getStorageQueue();
	
}
