package net.ess3.testserver;

import java.io.File;
import java.io.InputStream;
import java.util.logging.Logger;
import net.ess3.api.server.Location;
import net.ess3.api.server.Player;
import net.ess3.api.server.Plugin;

public class TestPlugin implements Plugin {

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
	public File getRootFolder()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public File getDataFolder()
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
	public InputStream getResource(String string)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public int scheduleAsyncRepeatingTask(Runnable run, long delay, long period)
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
	public Logger getLogger()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

}
