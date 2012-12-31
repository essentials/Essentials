package com.earth2me.essentials;

import java.io.File;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import net.ess3.api.IEssentials;
import net.ess3.api.IItemDb;
import net.ess3.api.IPlugin;


public class Essentials extends JavaPlugin
{
	IEssentials ess;

	@Override
	public void onEnable()
	{
		Bukkit.getLogger().info("You can remove this compatibility plugin, when all plugins are updated to Essentials-3");
		//TODO: Update files to new 3.0 format
		//TODO: Move Eco Api here
		IPlugin plugin = (IPlugin)getServer().getPluginManager().getPlugin("Essentials-3");
		ess = plugin.getEssentials();
		updateSettings();
		updateUserfiles();
	}

	private void updateSettings()
	{
		File config = new File(getDataFolder(), "config.yml");
		if (config.isFile())
		{
			new UpdateSettings(config, ess);
			File fileNew;
			do
			{
				fileNew = new File(getDataFolder(), "config-" + System.currentTimeMillis() + ".yml");
			}
			while (fileNew.exists());
			config.renameTo(fileNew);
		}
	}

	private void updateUserfiles()
	{
		File folder = new File(getDataFolder(), "userdata");

		if (folder.isDirectory())
		{
			new UpdateUserFiles(folder, ess);
			File folderNew;
			do
			{
				folderNew = new File(getDataFolder(), "userdata-" + System.currentTimeMillis());
			}
			while (folderNew.exists());
			folder.renameTo(folderNew);
		}
	}

	public IItemDb getItemDb()
	{
		return ess.getItemDb();
	}
}
