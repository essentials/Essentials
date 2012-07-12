package net.ess3.api;

import net.ess3.EssentialsTimer;
import net.ess3.economy.register.Methods;
import net.ess3.listener.TntExplodeListener;
import net.ess3.metrics.Metrics;
import java.util.List;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;


public interface IEssentials extends Plugin
{
	void addReloadListener(IReload listener);

	//IUser getUser(final Object base);
	IUser getUser(final Player player);
	
	IUser getUser(final String playerName);

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

	int scheduleAsyncDelayedTask(Runnable run);

	int scheduleSyncDelayedTask(Runnable run);

	int scheduleSyncDelayedTask(Runnable run, long delay);

	int scheduleSyncRepeatingTask(Runnable run, long delay, long period);

	//IPermissionsHandler getPermissionsHandler();
	void reload();

	TntExplodeListener getTNTListener();

	void setRanks(IRanks groups);

	void removeReloadListener(IReload groups);

	IEconomy getEconomy();
	
	List<String> getVanishedPlayers();
	
	EssentialsTimer getTimer();
	
	Metrics getMetrics();
	
	void setMetrics(Metrics metrics);
}
