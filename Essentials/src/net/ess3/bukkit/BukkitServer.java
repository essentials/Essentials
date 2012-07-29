package net.ess3.bukkit;

import java.util.*;
import lombok.Delegate;
import lombok.Getter;
import net.ess3.api.IEssentials;
import net.ess3.api.server.CommandSender;
import net.ess3.api.server.Player;
import net.ess3.api.server.Server;
import net.ess3.api.server.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldUnloadEvent;
import org.bukkit.plugin.RegisteredServiceProvider;


public class BukkitServer implements Server, Listener
{
	private IEssentials ess;
	private interface Excludes
	{
		List<org.bukkit.World> getWorlds();

		org.bukkit.World getWorld(String name);

		org.bukkit.entity.Player[] getOnlinePlayers();
		
		BukkitCommandSender getConsoleSender();
		
		org.bukkit.entity.Player getPlayer(String name);
	}
	@Delegate(excludes = Excludes.class)
	private final org.bukkit.Server server;
	@Getter
	private List<World> worlds;
	private Map<String, World> worldsMap;
	@Getter
	private Collection<Player> onlinePlayers;
	private Map<String, Player> onlinePlayersMap;
	@Getter
	private CommandSender consoleSender;

	public BukkitServer(final org.bukkit.Server server)
	{
		this.server = server;
		consoleSender = new BukkitCommandSender(server.getConsoleSender());
		updateWorlds();
	}
	
	private void updateWorlds()
	{
		final ArrayList<World> lworlds = new ArrayList<World>(server.getWorlds().size());
		final HashMap<String, World> lworldsMap = new HashMap<String, World>();
		for (org.bukkit.World world : server.getWorlds())
		{
			final BukkitWorld w = new BukkitWorld(world);
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
		final HashMap<String, Player> oplayersMap = new HashMap<String, Player>(onlinePlayersMap);
		BukkitPlayer p = new BukkitPlayer(event.getPlayer(), this);
		
		oplayersMap.put(event.getPlayer().getName(), p);
		onlinePlayersMap = Collections.unmodifiableMap(oplayersMap);
		onlinePlayers = Collections.unmodifiableCollection(oplayersMap.values());
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public synchronized void onPlayerQuitEvent(final PlayerQuitEvent event)
	{
		final ArrayList<Player> oplayers = new ArrayList<Player>(onlinePlayers);
		final HashMap<String, Player> oplayersMap = new HashMap<String, Player>(onlinePlayersMap);
		oplayersMap.remove(event.getPlayer().getName());
		onlinePlayersMap = Collections.unmodifiableMap(oplayersMap);
		onlinePlayers = Collections.unmodifiableCollection(oplayersMap.values());
	}

	@Override
	public World getWorld(final String name)
	{
		final World world = worldsMap.get(name);
		if (world == null)
		{
			return worldsMap.get(name.toLowerCase(Locale.ENGLISH));
		}
		return world;
	}
	
	@Override
	public void dispatchCommand(final CommandSender sender, final String command)
	{
		server.dispatchCommand(((BukkitCommandSender)sender).getCommandSender(), command);
	}
	
	@Override
	public <T> T getServiceProvider(final Class<T> clazz)
	{
		final RegisteredServiceProvider<T> rsp = server.getServicesManager().getRegistration(clazz);
		return rsp.getProvider();
	}
	
	public Player getPlayer(final org.bukkit.entity.Player player) {
		return onlinePlayersMap.get(player.getName());
	}
	
	@Override
	public Player getPlayer(final String playerName) {
		return onlinePlayersMap.get(playerName);
	}
}
