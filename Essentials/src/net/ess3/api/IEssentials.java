package net.ess3.api;

import net.ess3.api.server.Plugin;
import net.ess3.api.server.Server;
import net.ess3.api.server.World;
import net.ess3.economy.register.Methods;
import net.ess3.listener.TntExplodeListener;
import java.util.logging.Logger;
import java.util.List;
import net.ess3.EssentialsTimer;
import net.ess3.economy.register.Methods;
import net.ess3.listener.TntExplodeListener;
import net.ess3.metrics.Metrics;


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

	//int scheduleAsyncDelayedTask(Runnable run);
	//int scheduleSyncDelayedTask(Runnable run);
	//int scheduleSyncDelayedTask(Runnable run, long delay);
	//int scheduleSyncRepeatingTask(Runnable run, long delay, long period);
	//IPermissionsHandler getPermissionsHandler();
	//void reload();
	TntExplodeListener getTNTListener();

	void setRanks(IRanks groups);

	void removeReloadListener(IReload groups);

	IEconomy getEconomy();

	Server getServer();

	Logger getLogger();

	Plugin getPlugin();
	
	List<String> getVanishedPlayers();
	
	EssentialsTimer getTimer();
	
	Metrics getMetrics();
	
	void setMetrics(Metrics metrics);
}
