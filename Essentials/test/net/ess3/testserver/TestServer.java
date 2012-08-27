package net.ess3.testserver;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.command.ConsoleCommandSender;

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

}
