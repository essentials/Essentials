package com.earth2me.essentials.bukkit;

import com.earth2me.essentials.api.IEssentials;
import com.earth2me.essentials.api.server.ICommandSender;
import com.earth2me.essentials.api.server.Player;
import com.earth2me.essentials.api.server.IServer;
import com.earth2me.essentials.api.server.IWorld;
import java.util.*;
import lombok.Delegate;
import lombok.Getter;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldUnloadEvent;
import org.bukkit.plugin.RegisteredServiceProvider;


public class Server implements IServer, Listener
{
	private IEssentials ess;
	private interface Excludes
	{
		List<org.bukkit.World> getWorlds();

		org.bukkit.World getWorld(String name);

		org.bukkit.entity.Player[] getOnlinePlayers();
		
		CommandSender getConsoleSender();
	}
	@Delegate(excludes = Excludes.class)
	private final org.bukkit.Server server;
	@Getter
	private List<IWorld> worlds;
	private Map<String, IWorld> worldsMap;
	@Getter
	private Collection<Player> onlinePlayers;
	private Map<org.bukkit.entity.Player, Player> onlinePlayersMap;
	@Getter
	private ICommandSender consoleSender;

	public Server(final org.bukkit.Server server)
	{
		this.server = server;
		consoleSender = new CommandSender(server.getConsoleSender());
		updateWorlds();
	}
	
	private void updateWorlds()
	{
		final ArrayList<IWorld> lworlds = new ArrayList<IWorld>(server.getWorlds().size());
		final HashMap<String, IWorld> lworldsMap = new HashMap<String, IWorld>();
		for (org.bukkit.World world : server.getWorlds())
		{
			final World w = new World(world);
			lworlds.add(w);
			lworldsMap.put(world.getName(), w);
			lworldsMap.put(world.getName().toLowerCase(Locale.ENGLISH), w);
		}
		this.worlds = Collections.unmodifiableList(lworlds);
		this.worldsMap = Collections.unmodifiableMap(lworldsMap);
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public synchronized void onWorldLoadEvent(final WorldLoadEvent event)
	{
		updateWorlds();
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public synchronized void onWorldUnloadEvent(final WorldUnloadEvent event)
	{
		updateWorlds();
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public synchronized void onPlayerLoginEvent(final PlayerLoginEvent event)
	{
		final HashMap<org.bukkit.entity.Player, Player> oplayersMap = new HashMap<org.bukkit.entity.Player, Player>(onlinePlayersMap);
		BukkitPlayer p = new BukkitPlayer(event.getPlayer(), this);
		oplayersMap.put(event.getPlayer(), p);
		onlinePlayersMap = Collections.unmodifiableMap(oplayersMap);
		onlinePlayers = Collections.unmodifiableCollection(oplayersMap.values());
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public synchronized void onPlayerQuitEvent(final PlayerQuitEvent event)
	{
		final ArrayList<Player> oplayers = new ArrayList<Player>(onlinePlayers);
		final HashMap<org.bukkit.entity.Player, Player> oplayersMap = new HashMap<org.bukkit.entity.Player, Player>(onlinePlayersMap);
		oplayersMap.remove(event.getPlayer());
		onlinePlayersMap = Collections.unmodifiableMap(oplayersMap);
		onlinePlayers = Collections.unmodifiableCollection(oplayersMap.values());
	}

	@Override
	public IWorld getWorld(final String name)
	{
		final IWorld world = worldsMap.get(name);
		if (world == null)
		{
			return worldsMap.get(name.toLowerCase(Locale.ENGLISH));
		}
		return world;
	}
	
	@Override
	public void dispatchCommand(final ICommandSender sender, final String command)
	{
		server.dispatchCommand(((CommandSender)sender).getCommandSender(), command);
	}
	
	@Override
	public <T> T getServiceProvider(final Class<T> clazz)
	{
		final RegisteredServiceProvider<T> rsp = server.getServicesManager().getRegistration(clazz);
		return rsp.getProvider();
	}
	
	public Player getPlayer(final org.bukkit.entity.Player player) {
		return onlinePlayersMap.get(player);
	}
}
