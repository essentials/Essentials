package com.earth2me.essentials.chat;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.Util;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;


public class EssentialsChat extends JavaPlugin
{
	private static final Logger logger = Logger.getLogger("Minecraft");

	public EssentialsChat()
	{
		super();
	}

	public void onEnable()
	{
		PluginManager pm = getServer().getPluginManager();

		EssentialsChatWorker.checkFactions(pm);

		EssentialsChatPlayerListener playerListener = new EssentialsChatPlayerListener(getServer());
		pm.registerEvent(Type.PLAYER_JOIN, playerListener, Priority.Monitor, this);
		pm.registerEvent(Type.PLAYER_CHAT, playerListener, Priority.Highest, this);
		if (!this.getDescription().getVersion().equals(Essentials.getStatic().getDescription().getVersion())) {
			logger.log(Level.WARNING, Util.i18n("versionMismatchAll"));
		}
		logger.info(Util.format("loadinfo", this.getDescription().getName(), this.getDescription().getVersion(), Essentials.AUTHORS));
	}

	public void onDisable()
	{
	}
}
