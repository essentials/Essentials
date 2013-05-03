package net.ess3.update;

import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;


public class EssentialsUpdate extends JavaPlugin
{
	private EssentialsHelp essentialsHelp;
	private UpdateProcess updateProcess;

	@Override
	public void onEnable()
	{
		if (!getDataFolder().exists() && !getDataFolder().mkdirs())
		{
			getLogger().log(Level.SEVERE, "Could not create data folder: {0}", getDataFolder().getPath());
		}
		essentialsHelp = new EssentialsHelp(this);
		essentialsHelp.registerEvents();

		final UpdateCheck updateCheck = new UpdateCheck(this);
		updateCheck.checkForUpdates();
		updateProcess = new UpdateProcess(this, updateCheck);
		updateProcess.registerEvents();

		if (updateCheck.isEssentialsInstalled())
		{
			updateCheck.scheduleUpdateTask();
		}
		else
		{
			getLogger().info("Essentials is ready for installation. Join the game and follow the instructions.");
		}
	}

	@Override
	public void onDisable()
	{
		essentialsHelp.onDisable();
	}

	@Override
	public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args)
	{
		if (command.getName().equalsIgnoreCase("essentialsupdate"))
		{
			updateProcess.onCommand(sender);
		}
		if (command.getName().equalsIgnoreCase("essentialshelp"))
		{
			essentialsHelp.onCommand(sender);
		}
		return true;
	}
}
