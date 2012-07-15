package com.earth2me.essentials.api;

import com.earth2me.essentials.api.server.Player;
import com.earth2me.essentials.api.server.IPlugin;
import com.earth2me.essentials.api.server.IServer;
import com.earth2me.essentials.api.server.World;
import com.earth2me.essentials.economy.register.Methods;
import com.earth2me.essentials.listener.TntExplodeListener;
import java.util.logging.Logger;


public interface IEssentials extends IComponent
{
	void addReloadListener(IReload listener);

	IUser getUser(String playerName);

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

	IServer getServer();

	Logger getLogger();

	IPlugin getPlugin();
}
