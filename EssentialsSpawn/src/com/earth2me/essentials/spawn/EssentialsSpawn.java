package com.earth2me.essentials.spawn;

import com.earth2me.essentials.components.commands.CommandsComponent;
import static com.earth2me.essentials.I18nComponent._;
import com.earth2me.essentials.api.ICommandsComponent;
import com.earth2me.essentials.api.IContext;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;


public class EssentialsSpawn extends JavaPlugin
{
	private static final Logger LOGGER = Bukkit.getLogger();
	private transient IContext ess;
	private transient SpawnStorage spawns;
	private transient ICommandsComponent commandHandler;

	public void onEnable()
	{
		final PluginManager pluginManager = getServer().getPluginManager();
		ess = (IContext)pluginManager.getPlugin("Essentials3");
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

		commandHandler = new CommandsComponent(EssentialsSpawn.class.getClassLoader(), "com.earth2me.essentials.spawn.Command", "essentials.", spawns, ess);

		final EssentialsSpawnPlayerListener playerListener = new EssentialsSpawnPlayerListener(ess, spawns);
		pluginManager.registerEvent(PlayerRespawnEvent.class, playerListener, spawns.getRespawnPriority(), new EventExecutor()
		{
			@Override
			public void execute(final Listener ll, final Event event) throws EventException
			{
				((EssentialsSpawnPlayerListener)ll).onPlayerRespawn((PlayerRespawnEvent)event);
			}
		}, this);
		pluginManager.registerEvent(PlayerJoinEvent.class, playerListener, spawns.getRespawnPriority(), new EventExecutor()
		{
			@Override
			public void execute(final Listener ll, final Event event) throws EventException
			{
				((EssentialsSpawnPlayerListener)ll).onPlayerJoin((PlayerJoinEvent)event);
			}
		}, this);
	}

	public void onDisable()
	{
	}

	@Override
	public boolean onCommand(final CommandSender sender, final Command command,
							 final String commandLabel, final String[] args)
	{
		return commandHandler.handleCommand(sender, command, commandLabel, args);
	}
}
