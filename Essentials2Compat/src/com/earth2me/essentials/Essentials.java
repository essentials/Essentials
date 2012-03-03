package com.earth2me.essentials;

import com.earth2me.essentials.api.EssentialsPlugin;
import org.bukkit.Bukkit;


public class Essentials extends EssentialsPlugin
{
	@Override
	public void onEnable()
	{
		// Always call this FIRST to initialize context.
		super.onEnable();

		Bukkit.getLogger().info("You can remove this compatibility plugin, when all plugins are updated to Essentials 3");
		//TODO Update files to new 3.0 format
		//TODO Move Eco Api here
	}

	@Override
	public void onDisable()
	{
	}
}
