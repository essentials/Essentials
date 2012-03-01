package com.earth2me.essentials.components.backup;

import com.earth2me.essentials.api.IContext;
import com.earth2me.essentials.api.ISettingsComponent;
import com.earth2me.essentials.components.Component;
import static com.earth2me.essentials.components.i18n.I18nComponent.$;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import lombok.Cleanup;
import org.bukkit.command.CommandSender;


public class BackupComponent extends Component implements IBackupComponent
{
	private transient final AtomicBoolean running = new AtomicBoolean(false);
	private transient int taskId = -1;
	private transient final AtomicBoolean active = new AtomicBoolean(false);

	public BackupComponent(final IContext context)
	{
		super(context);

		if (getContext().getServer().getOnlinePlayers().length > 0)
		{
			startTask();
		}
	}

	@Override
	public final void startTask()
	{
		if (running.compareAndSet(false, true))
		{
			@Cleanup
			final ISettingsComponent settings = getContext().getSettings();
			settings.acquireReadLock();
			final long interval = settings.getData().getGeneral().getBackup().getInterval() * 1200; // minutes -> ticks
			if (interval < 1200)
			{
				running.set(false);
				return;
			}
			taskId = getContext().getScheduler().scheduleSyncRepeatingTask(this, interval, interval);
		}
	}

	@Override
	public void run()
	{
		if (!active.compareAndSet(false, true))
		{
			return;
		}
		@Cleanup
		final ISettingsComponent settings = getContext().getSettings();
		settings.acquireReadLock();
		final String command = settings.getData().getGeneral().getBackup().getCommand();
		if (command == null || command.isEmpty())
		{
			return;
		}
		getContext().getLogger().log(Level.INFO, $("backupStarted"));
		final CommandSender consoleSender = getContext().getServer().getConsoleSender();
		getContext().getServer().dispatchCommand(consoleSender, "save-all");
		getContext().getServer().dispatchCommand(consoleSender, "save-off");

		getContext().getScheduler().scheduleAsyncDelayedTask(new BackupRunner(command));
	}


	private class BackupRunner implements Runnable
	{
		private final transient String command;

		public BackupRunner(final String command)
		{
			this.command = command;
		}

		@Override
		public void run()
		{
			try
			{
				final ProcessBuilder childBuilder = new ProcessBuilder(command);
				childBuilder.redirectErrorStream(true);
				childBuilder.directory(getContext().getDataFolder().getParentFile().getParentFile());
				final Process child = childBuilder.start();
				final BufferedReader reader = new BufferedReader(new InputStreamReader(child.getInputStream()));
				try
				{
					child.waitFor();
					String line;
					do
					{
						line = reader.readLine();
						if (line != null)
						{
							getContext().getLogger().log(Level.INFO, line);
						}
					}
					while (line != null);
				}
				finally
				{
					reader.close();
				}
			}
			catch (InterruptedException ex)
			{
				getContext().getLogger().log(Level.SEVERE, null, ex);
			}
			catch (IOException ex)
			{
				getContext().getLogger().log(Level.SEVERE, null, ex);
			}
			finally
			{
				getContext().getScheduler().scheduleSyncDelayedTask(new EnableSavingRunner());
			}
		}
	}


	private class EnableSavingRunner implements Runnable
	{
		@Override
		public void run()
		{
			getContext().getServer().dispatchCommand(getContext().getServer().getConsoleSender(), "save-on");
			if (getContext().getServer().getOnlinePlayers().length == 0)
			{
				running.set(false);
				if (taskId != -1)
				{
					getContext().getServer().getScheduler().cancelTask(taskId);
				}
			}

			active.set(false);
			getContext().getLogger().log(Level.INFO, $("backupFinished"));
		}
	}
}
