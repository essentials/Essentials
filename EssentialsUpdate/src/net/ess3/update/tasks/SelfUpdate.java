package net.ess3.update.tasks;

import net.ess3.update.AbstractWorkListener;
import org.bukkit.Bukkit;


public class SelfUpdate extends AbstractWorkListener implements Task, Runnable
{
	private final transient AbstractWorkListener listener;

	public SelfUpdate(final AbstractWorkListener listener)
	{
		super(listener.getPlugin(), listener.getNewVersionInfo());
		this.listener = listener;
	}

	@Override
	public void onWorkAbort(final String message)
	{
		listener.onWorkAbort(message);
	}

	@Override
	public void onWorkDone(final String message)
	{
		listener.onWorkDone(message);
		Bukkit.getScheduler().scheduleSyncDelayedTask(
				getPlugin(), new Runnable()
		{
			@Override
			public void run()
			{
				Bukkit.getServer().reload();
			}
		});
	}

	@Override
	public void start()
	{
		Bukkit.getScheduler().scheduleSyncDelayedTask(getPlugin(), this);
	}

	@Override
	public void run()
	{
		Bukkit.getScheduler().scheduleAsyncDelayedTask( //Should this be async? (method deprecated)
				getPlugin(), new Runnable()
		{
			@Override
			public void run()
			{
				new InstallModule(SelfUpdate.this, "EssentialsUpdate").start();
			}
		});
	}
}
