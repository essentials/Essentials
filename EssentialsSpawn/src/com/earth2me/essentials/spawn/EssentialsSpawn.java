package com.earth2me.essentials.spawn;

import com.earth2me.essentials.api.EssentialsPlugin;
import com.earth2me.essentials.api.ICommandsComponent;
import com.earth2me.essentials.api.IContext;
import com.earth2me.essentials.components.commands.CommandsComponent;
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


public class EssentialsSpawn extends EssentialsPlugin
{
	private static final Logger LOGGER = Bukkit.getLogger();
	private transient IContext context;
	private transient SpawnStorageComponent spawns;
	private transient ICommandsComponent commandHandler;

	@Override
	public void onEnable()
	{
		final PluginManager pluginManager = getServer().getPluginManager();

		spawns = new SpawnStorageComponent(context, this);
		context.getEssentials().add(spawns);

		commandHandler = new CommandsComponent(EssentialsSpawn.class.getClassLoader(), "com.earth2me.essentials.spawn.Command", "essentials.", spawns, context);

		final EssentialsSpawnPlayerListener playerListener = new EssentialsSpawnPlayerListener(context, spawns);
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

	@Override
	public boolean onCommand(final CommandSender sender, final Command command,
							 final String commandLabel, final String[] args)
	{
		return commandHandler.handleCommand(sender, command, commandLabel, args);
	}
}
