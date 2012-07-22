package net.ess3.testserver;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import net.ess3.api.server.CommandSender;
import net.ess3.api.server.Player;
import net.ess3.api.server.Server;
import net.ess3.api.server.World;

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
	public Collection<Player> getOnlinePlayers()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public CommandSender getConsoleSender()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void dispatchCommand(CommandSender sender, String command)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void banIP(String ip)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public <T> T getServiceProvider(Class<T> clazz)
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

}
