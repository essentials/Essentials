package net.ess3.testserver;

import com.avaje.ebean.EbeanServer;
import java.io.File;
import java.io.InputStream;
import java.util.logging.Logger;
import net.ess3.api.IEssentials;
import net.ess3.api.IPlugin;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginLoader;

public class TestPlugin implements IPlugin {

	
	@Override
	public File getDataFolder()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}
	
	@Override
	public InputStream getResource(String string)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}
	@Override
	public Server getServer()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public IEssentials getEssentials()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public int scheduleAsyncDelayedTask(Runnable run)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public int scheduleSyncDelayedTask(Runnable run)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public int scheduleSyncDelayedTask(Runnable run, long delay)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public int scheduleSyncRepeatingTask(Runnable run, long delay, long period)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public int scheduleAsyncRepeatingTask(Runnable run, long delay, long period)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public File getRootFolder()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void cancelTask(int taskId)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public String getVersion()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public Class getClassByName(String name)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public Location callRespawnEvent(Player player, Location loc, boolean bedSpawn)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void callSuicideEvent(Player player)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public PluginDescriptionFile getDescription()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public FileConfiguration getConfig()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void saveConfig()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void saveDefaultConfig()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void saveResource(String string, boolean bln)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void reloadConfig()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public PluginLoader getPluginLoader()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public boolean isEnabled()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void onDisable()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void onLoad()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void onEnable()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public boolean isNaggable()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void setNaggable(boolean bln)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public EbeanServer getDatabase()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public ChunkGenerator getDefaultWorldGenerator(String string, String string1)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public Logger getLogger()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public String getName()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public boolean onCommand(CommandSender cs, Command cmnd, String string, String[] strings)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

}
