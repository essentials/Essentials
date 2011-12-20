package com.earth2me.essentials.spawn;

import static com.earth2me.essentials.I18n._;
import com.earth2me.essentials.IEssentials;
import com.earth2me.essentials.IEssentialsModule;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;


public class EssentialsSpawn extends JavaPlugin
{
	private static final Logger LOGGER = Bukkit.getLogger();
	private transient IEssentials ess;
	private transient SpawnStorage spawns;

	public void onEnable()
	{
		final PluginManager pluginManager = getServer().getPluginManager();
		ess = (IEssentials)pluginManager.getPlugin("Essentials");
		if (!this.getDescription().getVersion().equals(ess.getDescription().getVersion()))
		{
			LOGGER.log(Level.WARNING, _("versionMismatchAll"));
		}
		if (!ess.isEnabled())
		{
			this.setEnabled(false);
			return;
		}

		spawns = new SpawnStorage(ess);
		ess.addReloadListener(spawns);

		final EssentialsSpawnPlayerListener playerListener = new EssentialsSpawnPlayerListener(ess, spawns);
		pluginManager.registerEvent(Type.PLAYER_RESPAWN, playerListener, ess.getSettings().getRespawnPriority(), this);
		pluginManager.registerEvent(Type.PLAYER_JOIN, playerListener, Priority.Low, this);

		LOGGER.info(_("loadinfo", this.getDescription().getName(), this.getDescription().getVersion(), "essentials team"));
	}

	public void onDisable()
	{
	}

	@Override
	public boolean onCommand(final CommandSender sender, final Command command,
							 final String commandLabel, final String[] args)
	{
		return ess.onCommandEssentials(sender, command, commandLabel, args, EssentialsSpawn.class.getClassLoader(), "com.earth2me.essentials.spawn.Command", "essentials.", spawns);
	}
}
