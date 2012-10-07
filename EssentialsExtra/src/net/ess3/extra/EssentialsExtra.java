package net.ess3.extra;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.logging.Level;
import net.ess3.api.ICommandHandler;
import net.ess3.api.IEssentials;
import net.ess3.bukkit.BukkitPlugin;
import net.ess3.commands.EssentialsCommandHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;


public class EssentialsExtra extends JavaPlugin
{
	private IEssentials ess;
	private ICommandHandler handler;
	private ClassLoader loader;

	@Override
	public void onLoad()
	{
		ess = ((BukkitPlugin)getServer().getPluginManager().getPlugin("Essentials-3")).getEssentials();
	}

	@Override
	public void onEnable()
	{
		handler = new EssentialsCommandHandler(loader, "Command", "essentials.", ess);
		File commandDir = new File(ess.getPlugin().getDataFolder(), "extras");
		commandDir.mkdir();
		URL[] urls = null;
		try
		{
			urls = new URL[]
			{
				commandDir.toURI().toURL()
			};
		}
		catch (MalformedURLException ex)
		{
			getLogger().log(Level.SEVERE, "Could not get extra command dir", ex);
			getServer().getPluginManager().disablePlugin(this);
		}
		loader = new URLClassLoader(urls, getClassLoader());
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		return handler.handleCommand(sender, command, label, args);
	}
}
