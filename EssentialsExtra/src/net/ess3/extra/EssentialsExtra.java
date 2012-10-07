package net.ess3.extra;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;
import net.ess3.api.ICommandHandler;
import net.ess3.api.IEssentials;
import net.ess3.bukkit.BukkitPlugin;
import net.ess3.commands.EssentialsCommandHandler;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.SimplePluginManager;
import org.bukkit.plugin.java.JavaPlugin;


public class EssentialsExtra extends JavaPlugin
{
	private CommandMap commandMap;

	@Override
	public void onEnable()
	{
		final IEssentials ess = ((BukkitPlugin)getServer().getPluginManager().getPlugin("Essentials-3")).getEssentials();
		File commandDir = new File(ess.getPlugin().getDataFolder(), "extras");
		commandDir.mkdir();

		URL[] urls = null;
		try
		{
			PluginManager pm = Bukkit.getServer().getPluginManager();
			Field f = SimplePluginManager.class.getDeclaredField("commandMap");
			f.setAccessible(true);
			commandMap = (CommandMap)f.get(pm);

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
		catch (Exception ex)
		{
			getLogger().log(Level.SEVERE, "Enable " + getName(), ex);
			getServer().getPluginManager().disablePlugin(this);
		}

		ClassLoader loader = new URLClassLoader(urls, getClassLoader());
		final ICommandHandler handler = new EssentialsCommandHandler(loader, "Command", "essentials.", ess);;
		for (File file : commandDir.listFiles())
		{
			String fileName = file.getName();
			if (fileName.startsWith("Command") && fileName.endsWith(".class"))
			{
				String commandName = fileName.substring(7, fileName.length() - 7);
				try
				{
					AnnotatedCommand anot = Class.forName(fileName).getAnnotation(AnnotatedCommand.class);
					if (anot == null)
					{
						throw new IllegalArgumentException("Command class is not annotated with AnnotatedCommand.class");
					}
					commandMap.register("Essentials", new Command(commandName, anot.description(), anot.usage(), Arrays.asList(anot.aliases()))
					{
						@Override
						public boolean execute(CommandSender cs, String label, String[] args)
						{
							return handler.handleCommand(cs, this, label, args);
						}
					});
					getLogger().info("Loaded command " + commandName);
				}
				catch (Exception ex)
				{
					getLogger().log(Level.SEVERE, "Could not register " + fileName, ex);
				}
			}
		}
	}
}
