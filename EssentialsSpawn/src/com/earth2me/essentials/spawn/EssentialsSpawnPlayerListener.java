package com.earth2me.essentials.spawn;

import com.earth2me.essentials.api.IContext;
import com.earth2me.essentials.api.ISettingsComponent;
import static com.earth2me.essentials.components.i18n.I18nComponent.$;
import com.earth2me.essentials.components.users.IUserComponent;
import com.earth2me.essentials.textreader.IText;
import com.earth2me.essentials.textreader.KeywordReplacer;
import com.earth2me.essentials.textreader.SimpleTextInput;
import com.earth2me.essentials.textreader.SimpleTextPager;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;


public class EssentialsSpawnPlayerListener implements Listener
{
	private final transient IContext context;
	private final transient SpawnStorageComponent spawns;

	public EssentialsSpawnPlayerListener(final IContext ess, final SpawnStorageComponent spawns)
	{
		super();
		this.context = ess;
		this.spawns = spawns;
	}

	public void onPlayerRespawn(final PlayerRespawnEvent event)
	{
		final IUserComponent user = context.getUser(event.getPlayer());

		boolean respawnAtHome = false;
		final ISettingsComponent settings = context.getSettings();
		settings.acquireReadLock();
		try
		{
			respawnAtHome = context.getSettings().getData().getCommands().getHome().isRespawnAtHome();
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
		final Location spawn = spawns.getSpawn(context.getGroups().getMainGroup(user));
		if (spawn != null)
		{
			event.setRespawnLocation(spawn);
		}
	}

	public void onPlayerJoin(final PlayerJoinEvent event)
	{
		final IUserComponent user = context.getUser(event.getPlayer());

		if (user.hasPlayedBefore())
		{
			return;
		}

		if (spawns.getNewbieSpawn() != null)
		{
			context.getScheduler().scheduleSyncDelayedTask(new NewPlayerTeleport(user), 1L);
		}

		if (spawns.getAnnounceNewPlayers())
		{
			final IText output = new KeywordReplacer(new SimpleTextInput(spawns.getAnnounceNewPlayerFormat(user)), user, context);
			final SimpleTextPager pager = new SimpleTextPager(output);
			context.getMessager().broadcastMessage(user, pager.getString(0));
		}
	}


	private class NewPlayerTeleport implements Runnable
	{
		private final transient IUserComponent user;

		public NewPlayerTeleport(final IUserComponent user)
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
					user.getTeleporter().now(spawn, false, TeleportCause.PLUGIN);
				}
			}
			catch (Exception ex)
			{
				Bukkit.getLogger().log(Level.WARNING, $("teleportNewPlayerError"), ex);
			}
		}
	}
}
