package net.ess3.api.server;

import java.util.Collection;
import java.util.List;


public interface Server
{
	List<World> getWorlds();

	World getWorld(String name);

	int broadcastMessage(String message);

	Collection<Player> getOnlinePlayers();
	
	CommandSender getConsoleSender();
	
	void dispatchCommand(CommandSender sender, String command);
	
	void banIP(String ip);
	
	<T> T getServiceProvider(Class<T> clazz);
	
	String getVersion();

	public void unbanIP(String string);
	
	public Player getPlayer(String name);
}
