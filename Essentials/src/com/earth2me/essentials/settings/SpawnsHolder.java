package com.earth2me.essentials.settings;

import static com.earth2me.essentials.I18n._;
import com.earth2me.essentials.api.IEssentials;
import com.earth2me.essentials.api.IEssentialsModule;
import com.earth2me.essentials.api.ISettings;
import com.earth2me.essentials.api.IUser;
import com.earth2me.essentials.storage.AsyncStorageObjectHolder;
import com.earth2me.essentials.storage.Location.WorldNotLoadedException;
import com.earth2me.essentials.utils.textreader.IText;
import com.earth2me.essentials.utils.textreader.KeywordReplacer;
import com.earth2me.essentials.utils.textreader.SimpleTextInput;
import com.earth2me.essentials.utils.textreader.SimpleTextPager;
import java.io.File;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import org.bukkit.*;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.EventExecutor;


public class SpawnsHolder extends AsyncStorageObjectHolder<Spawns> implements IEssentialsModule
{
	public SpawnsHolder(final IEssentials ess)
	{
		super(ess, Spawns.class);
		onReload();
		registerListeners();
	}

	@Override
	public File getStorageFile()
	{
		return new File(ess.getDataFolder(), "spawn.yml");
	}

	public void setSpawn(final Location loc, final String group)
	{
		acquireWriteLock();
		try
		{
			if (getData().getSpawns() == null)
			{
				getData().setSpawns(new HashMap<String, com.earth2me.essentials.storage.Location>());
			}
			getData().getSpawns().put(group.toLowerCase(Locale.ENGLISH), new com.earth2me.essentials.storage.Location(loc));
		}
		finally
		{
			unlock();
		}

		if ("default".equalsIgnoreCase(group))
		{
			loc.getWorld().setSpawnLocation(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
		}
	}

	public Location getSpawn(final String group)
	{
		acquireReadLock();
		try
		{
			if (getData().getSpawns() == null || group == null)
			{
				return getWorldSpawn();
			}
			final Map<String, com.earth2me.essentials.storage.Location> spawnMap = getData().getSpawns();
			String groupName = group.toLowerCase(Locale.ENGLISH);
			if (!spawnMap.containsKey(groupName))
			{
				groupName = "default";
			}
			if (!spawnMap.containsKey(groupName))
			{
				return getWorldSpawn();
			}
			try
			{
				return spawnMap.get(groupName).getBukkitLocation();
			}
			catch (WorldNotLoadedException ex)
			{
				return getWorldSpawn();
			}
		}
		finally
		{
			unlock();
		}
	}

	private Location getWorldSpawn()
	{
		for (World world : ess.getServer().getWorlds())
		{
			if (world.getEnvironment() != World.Environment.NORMAL)
			{
				continue;
			}
			return world.getSpawnLocation();
		}
		return ess.getServer().getWorlds().get(0).getSpawnLocation();
	}

	public EventPriority getRespawnPriority()
	{
		acquireReadLock();
		try
		{
			for (EventPriority priority : EventPriority.values())
			{
				if (priority.toString().equalsIgnoreCase(getData().getRespawnPriority()))
				{
					return priority;
				}
			}
			return EventPriority.NORMAL;
		}
		finally
		{
			unlock();
		}
	}

	public Location getNewbieSpawn()
	{
		acquireReadLock();
		try
		{
			if (getData().getNewbieSpawn() == null || getData().getNewbieSpawn().isEmpty()
				|| getData().getNewbieSpawn().equalsIgnoreCase("none"))
			{
				return null;
			}
			return getSpawn(getData().getNewbieSpawn());
		}
		finally
		{
			unlock();
		}
	}

	public boolean getAnnounceNewPlayers()
	{
		acquireReadLock();
		try
		{
			return getData().getNewPlayerAnnouncement() != null && !getData().getNewPlayerAnnouncement().isEmpty();
		}
		finally
		{
			unlock();
		}
	}

	public String getAnnounceNewPlayerFormat(IUser user)
	{
		acquireReadLock();
		try
		{
			return getData().getNewPlayerAnnouncement().replace('&', '�').replace("��", "&").replace("{PLAYER}", user.getDisplayName()).replace("{DISPLAYNAME}", user.getDisplayName()).replace("{GROUP}", ess.getRanks().getMainGroup(user)).replace("{USERNAME}", user.getName()).replace("{ADDRESS}", user.getAddress().toString());
		}
		finally
		{
			unlock();
		}
	}

	private void registerListeners()
	{
		final SpawnPlayerListener playerListener = new SpawnPlayerListener(ess, this);
		ess.getServer().getPluginManager().registerEvent(PlayerRespawnEvent.class, playerListener, getRespawnPriority(), new EventExecutor()
		{
			@Override
			public void execute(final Listener ll, final Event event) throws EventException
			{
				((SpawnPlayerListener)ll).onPlayerRespawn((PlayerRespawnEvent)event);
			}
		}, ess);
		ess.getServer().getPluginManager().registerEvent(PlayerJoinEvent.class, playerListener, getRespawnPriority(), new EventExecutor()
		{
			@Override
			public void execute(final Listener ll, final Event event) throws EventException
			{
				((SpawnPlayerListener)ll).onPlayerJoin((PlayerJoinEvent)event);
			}
		}, ess);
	}


	private class SpawnPlayerListener implements Listener
	{
		private final transient IEssentials ess;
		private final transient SpawnsHolder spawns;

		public SpawnPlayerListener(final IEssentials ess, final SpawnsHolder spawns)
		{
			super();
			this.ess = ess;
			this.spawns = spawns;
		}

		public void onPlayerRespawn(final PlayerRespawnEvent event)
		{
			final IUser user = ess.getUser(event.getPlayer());

			boolean respawnAtHome = false;
			final ISettings settings = ess.getSettings();
			settings.acquireReadLock();
			try
			{
				respawnAtHome = ess.getSettings().getData().getCommands().getHome().isRespawnAtHome();
			}
			finally
			{
				settings.unlock();
			}
			if (respawnAtHome)
			{
				Location home;
				final Location bed = user.getBedSpawnLocation();
				if (bed != null && bed.getBlock().getType() == Material.BED_BLOCK)
				{
					home = bed;
				}
				else
				{
					home = user.getHome(user.getLocation());
				}
				if (home != null)
				{
					event.setRespawnLocation(home);
					return;
				}
			}
			final Location spawn = spawns.getSpawn(ess.getRanks().getMainGroup(user));
			if (spawn != null)
			{
				event.setRespawnLocation(spawn);
			}
		}

		public void onPlayerJoin(final PlayerJoinEvent event)
		{
			final IUser user = ess.getUser(event.getPlayer());

			if (user.hasPlayedBefore())
			{
				return;
			}

			if (spawns.getNewbieSpawn() != null)
			{
				ess.scheduleSyncDelayedTask(new NewPlayerTeleport(user), 1L);
			}

			if (spawns.getAnnounceNewPlayers())
			{
				final IText output = new KeywordReplacer(new SimpleTextInput(spawns.getAnnounceNewPlayerFormat(user)), user, ess);
				final SimpleTextPager pager = new SimpleTextPager(output);
				ess.broadcastMessage(user, pager.getString(0));
			}
		}


		private class NewPlayerTeleport implements Runnable
		{
			private final transient IUser user;

			public NewPlayerTeleport(final IUser user)
			{
				this.user = user;
			}

			@Override
			public void run()
			{
				if (user.getBase() instanceof OfflinePlayer)
				{
					return;
				}

				try
				{
					final Location spawn = spawns.getNewbieSpawn();
					if (spawn != null)
					{
						user.getTeleport().now(spawn, false, PlayerTeleportEvent.TeleportCause.PLUGIN);
					}
				}
				catch (Exception ex)
				{
					Bukkit.getLogger().log(Level.WARNING, _("teleportNewPlayerError"), ex);
				}
			}
		}
	}
}
