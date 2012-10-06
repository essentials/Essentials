package net.ess3.settings;

import java.io.File;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import static net.ess3.I18n._;
import net.ess3.api.IEssentials;
import net.ess3.api.IEssentialsModule;
import net.ess3.api.ISettings;
import net.ess3.api.IUser;
import net.ess3.storage.AsyncStorageObjectHolder;
import net.ess3.storage.StoredLocation.WorldNotLoadedException;
import net.ess3.utils.textreader.IText;
import net.ess3.utils.textreader.KeywordReplacer;
import net.ess3.utils.textreader.SimpleTextInput;
import net.ess3.utils.textreader.SimpleTextPager;
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
	@Override
	public void finishRead()
	{
	}

	@Override
	public void finishWrite()
	{
	}

	public SpawnsHolder(final IEssentials ess)
	{
		super(ess, Spawns.class);
		onReload();
		registerListeners();
	}

	@Override
	public File getStorageFile()
	{
		return new File(ess.getPlugin().getDataFolder(), "spawn.yml");
	}

	public void setSpawn(final Location loc, final String group)
	{
		getData().addSpawn(group, loc);
		queueSave();

		if ("default".equalsIgnoreCase(group))
		{
			loc.getWorld().setSpawnLocation(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
		}
	}

	public Location getSpawn(final String group)
	{
		if (getData().getSpawns() == null || group == null)
		{
			return getWorldSpawn();
		}
		final Map<String, net.ess3.storage.StoredLocation> spawnMap = getData().getSpawns();
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
			return spawnMap.get(groupName).getStoredLocation();
		}
		catch (WorldNotLoadedException ex)
		{
			return getWorldSpawn();
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
		for (EventPriority priority : EventPriority.values())
		{
			if (priority.toString().equalsIgnoreCase(getData().getRespawnPriority()))
			{
				return priority;
			}
		}
		return EventPriority.NORMAL;
	}

	public Location getNewbieSpawn()
	{
		if (getData().getNewbieSpawn() == null || getData().getNewbieSpawn().isEmpty()
			|| getData().getNewbieSpawn().equalsIgnoreCase("none"))
		{
			return null;
		}
		return getSpawn(getData().getNewbieSpawn());
	}

	public boolean getAnnounceNewPlayers()
	{
		return getData().getNewPlayerAnnouncement() != null && !getData().getNewPlayerAnnouncement().isEmpty();
	}

	public String getAnnounceNewPlayerFormat(IUser user)
	{
		return getData().getNewPlayerAnnouncement().replace('&', '\u00a7').replace("\u00a7\u00a7", "&").replace("{PLAYER}", user.getPlayer().getDisplayName()).replace("{DISPLAYNAME}", user.getPlayer().getDisplayName()).replace("{GROUP}", ess.getRanks().getMainGroup(user)).replace("{USERNAME}", user.getName()).replace("{ADDRESS}", user.getPlayer().getAddress().toString());
	}

	//TODO: Why is this stuff here in the settings folder?
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
		}, ess.getPlugin());
		ess.getServer().getPluginManager().registerEvent(PlayerJoinEvent.class, playerListener, getRespawnPriority(), new EventExecutor()
		{
			@Override
			public void execute(final Listener ll, final Event event) throws EventException
			{
				((SpawnPlayerListener)ll).onPlayerJoin((PlayerJoinEvent)event);
			}
		}, ess.getPlugin());
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
			final IUser user = ess.getUserMap().getUser(event.getPlayer());

			final ISettings settings = ess.getSettings();
			boolean respawnAtHome = ess.getSettings().getData().getCommands().getHome().isRespawnAtHome();
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
					home = user.getHome(user.getPlayer().getLocation());
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
			final IUser user = ess.getUserMap().getUser(event.getPlayer());

			if (user.hasPlayedBefore())
			{
				return;
			}

			if (spawns.getNewbieSpawn() != null)
			{
				ess.getPlugin().scheduleSyncDelayedTask(new NewPlayerTeleport(user), 1L);
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
				if (user.getPlayer() instanceof OfflinePlayer)
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
