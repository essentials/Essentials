package net.ess3.extra;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
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

	@Override
	public void onLoad()
	{
		ess = ((BukkitPlugin)getServer().getPluginManager().getPlugin("Essentials-3")).getEssentials();
	}

	@Override
	public void onEnable()
	{
		File commandDir = new File(ess.getPlugin().getDataFolder(), "extras");
		commandDir.mkdir();

		URL[] urls = null;
		try
		{
			JarFile jar = new JarFile(getFile());
			Enumeration<JarEntry> entries = jar.entries();
			while (entries.hasMoreElements())
			{
				JarEntry entry = entries.nextElement();
				String name = entry.getName();
				if (name.startsWith("Command") && name.endsWith(".class"))
				{
					File outFile = new File(commandDir, name);
					if (!outFile.exists())
					{
						InputStream is = jar.getInputStream(entry);
						OutputStream os = new FileOutputStream(outFile);
						byte[] buffer = new byte[4096];
						int length;
						while ((length = is.read(buffer)) > 0)
						{
							os.write(buffer, 0, length);
						}
						os.close();
						is.close();

					}
				}
			}
			urls = new URL[]
			{
				commandDir.toURI().toURL()
			};
		}
		catch (IOException ex)
		{
			getLogger().log(Level.SEVERE, "Could not get extra command dir", ex);
			getServer().getPluginManager().disablePlugin(this);
		}

		for (File file : commandDir.listFiles())
		{
			String name = file.getName();
			if (name.startsWith("Command") && name.endsWith(".class"))
			{
				getLogger().info("Loaded command " + name.substring(0, name.length() - 7));
			}
		}

		ClassLoader loader = new URLClassLoader(urls, getClassLoader());
		handler = new EssentialsCommandHandler(loader, "Command", "essentials.", ess);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		return handler.handleCommand(sender, command, label, args);
	}
}
