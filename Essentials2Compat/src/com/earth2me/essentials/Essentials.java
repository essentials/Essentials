package com.earth2me.essentials;

import java.io.File;
import java.util.Calendar;
import java.util.GregorianCalendar;
import net.ess3.api.IEssentials;
import net.ess3.api.IPlugin;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;


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
		updateUserfiles();
	}

	private void updateUserfiles()
	{
		File folder = new File(getDataFolder(), "userdata");
		
		if (folder.isDirectory()) {
			new UpdateUserFiles(folder, ess);
			File folderNew = new File(getDataFolder(), "userdata-"+System.currentTimeMillis());
			if (!folderNew.exists()) {
				folder.renameTo(folderNew);
			}
		}
	}
}
