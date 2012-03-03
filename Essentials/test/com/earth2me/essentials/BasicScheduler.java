package com.earth2me.essentials;

import com.earth2me.essentials.api.IScheduler;

public class BasicScheduler implements IScheduler {

	public BasicScheduler()
	{
	}
	
	

	@Override
	public int scheduleAsyncDelayedTask(Runnable run)
	{
		run.run();
		return 0;
	}

	@Override
	public int scheduleSyncDelayedTask(Runnable run)
	{
		run.run();
		return 0;
	}

	@Override
	public int scheduleSyncDelayedTask(Runnable run, long delay)
	{
		run.run();
		return 0;
	}

	@Override
	public int scheduleSyncRepeatingTask(Runnable run, long delay, long period)
	{
		run.run();
		return 0;
	}

	
}
