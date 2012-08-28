package net.ess3.testserver;

import com.avaje.ebean.config.ServerConfig;
import java.io.File;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Logger;
import org.bukkit.GameMode;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.Server;
import org.bukkit.Warning.WarningState;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.help.HelpMap;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.map.MapView;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.messaging.Messenger;
import org.bukkit.scheduler.BukkitScheduler;

public class TestServer implements Server {

	public TestServer()
	{
	}
	
	

	@Override
	public List<World> getWorlds()
	{
		return Collections.<World>singletonList(new TestWorld());
	}

	@Override
	public World getWorld(final String name)
	{
		final World world = getWorlds().get(0); 
		if (name.equals(world.getName())) {
			return world;
		} else {
			return null;
		}
	}
	
	public void addPlayer(final String name)
	{
		
	}

	@Override
	public int broadcastMessage(String message)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public Player[] getOnlinePlayers()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public ConsoleCommandSender getConsoleSender()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public boolean dispatchCommand(CommandSender sender, String command)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void banIP(String ip)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public String getVersion()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void unbanIP(String string)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public Player getPlayer(String name)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public String getName()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public String getBukkitVersion()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public int getMaxPlayers()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public int getPort()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public int getViewDistance()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public String getIp()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public String getServerName()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public String getServerId()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public String getWorldType()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public boolean getGenerateStructures()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public boolean getAllowEnd()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public boolean getAllowNether()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public boolean hasWhitelist()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void setWhitelist(boolean bln)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public Set<OfflinePlayer> getWhitelistedPlayers()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void reloadWhitelist()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public String getUpdateFolder()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public File getUpdateFolderFile()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public long getConnectionThrottle()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public int getTicksPerAnimalSpawns()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public int getTicksPerMonsterSpawns()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public Player getPlayerExact(String string)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public List<Player> matchPlayer(String string)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public PluginManager getPluginManager()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public BukkitScheduler getScheduler()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public ServicesManager getServicesManager()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public World createWorld(WorldCreator wc)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public boolean unloadWorld(String string, boolean bln)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public boolean unloadWorld(World world, boolean bln)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public World getWorld(UUID uuid)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public MapView getMap(short s)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public MapView createMap(World world)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void reload()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public Logger getLogger()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public PluginCommand getPluginCommand(String string)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void savePlayers()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void configureDbConfig(ServerConfig sc)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public boolean addRecipe(Recipe recipe)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public List<Recipe> getRecipesFor(ItemStack is)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public Iterator<Recipe> recipeIterator()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void clearRecipes()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void resetRecipes()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public Map<String, String[]> getCommandAliases()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public int getSpawnRadius()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void setSpawnRadius(int i)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public boolean getOnlineMode()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public boolean getAllowFlight()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public boolean useExactLoginLocation()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void shutdown()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public int broadcast(String string, String string1)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public OfflinePlayer getOfflinePlayer(String string)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public Set<String> getIPBans()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public Set<OfflinePlayer> getBannedPlayers()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public Set<OfflinePlayer> getOperators()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public GameMode getDefaultGameMode()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void setDefaultGameMode(GameMode gm)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public File getWorldContainer()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public OfflinePlayer[] getOfflinePlayers()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public Messenger getMessenger()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public HelpMap getHelpMap()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public Inventory createInventory(InventoryHolder ih, InventoryType it)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public Inventory createInventory(InventoryHolder ih, int i)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public Inventory createInventory(InventoryHolder ih, int i, String string)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public int getMonsterSpawnLimit()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public int getAnimalSpawnLimit()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public int getWaterAnimalSpawnLimit()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public boolean isPrimaryThread()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public String getMotd()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public WarningState getWarningState()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void sendPluginMessage(Plugin plugin, String string, byte[] bytes)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public Set<String> getListeningPluginChannels()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

}
