package com.earth2me.essentials.testserver;

import com.earth2me.essentials.api.server.IPlugin;
import java.io.File;
import java.io.InputStream;

public class Plugin implements IPlugin {

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

}
