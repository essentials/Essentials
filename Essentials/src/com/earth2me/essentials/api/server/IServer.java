package com.earth2me.essentials.api.server;

import java.util.Collection;
import java.util.List;


public interface IServer
{
	List<IWorld> getWorlds();

	IWorld getWorld(String name);

	int broadcastMessage(String message);

	Collection<Player> getOnlinePlayers();
	
	ICommandSender getConsoleSender();
	
	void dispatchCommand(ICommandSender sender, String command);
	
	void banIP(String ip);
	
	<T> T getServiceProvider(Class<T> clazz);
	
	String getVersion();

	public void unbanIP(String string);
}
