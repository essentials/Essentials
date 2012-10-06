package net.ess3;

import java.io.File;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.Cleanup;
import static net.ess3.I18n._;
import net.ess3.api.IEssentials;
import net.ess3.api.IJails;
import net.ess3.api.IUser;
import net.ess3.storage.AsyncStorageObjectHolder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.plugin.PluginManager;


public class Jails extends AsyncStorageObjectHolder<net.ess3.settings.Jails> implements IJails
{
	private static final transient Logger LOGGER = Bukkit.getLogger();

	public Jails(final IEssentials ess)
	{
		super(ess, net.ess3.settings.Jails.class);
		onReload();
		registerListeners();
	}

	private void registerListeners()
	{
		final PluginManager pluginManager = ess.getServer().getPluginManager();
		final JailBlockListener blockListener = new JailBlockListener();
		final JailPlayerListener playerListener = new JailPlayerListener();
		pluginManager.registerEvents(blockListener, ess.getPlugin());
		pluginManager.registerEvents(playerListener, ess.getPlugin());
	}

	@Override
	public File getStorageFile()
	{
		return new File(ess.getPlugin().getDataFolder(), "jail.yml");
	}

	@Override
	public Location getJail(final String jailName) throws Exception
	{
		acquireReadLock();
		try
		{
			if (getData().getJails() == null || jailName == null
				|| !getData().getJails().containsKey(jailName.toLowerCase(Locale.ENGLISH)))
			{
				throw new Exception(_("jailNotExist"));
			}
			Location loc = getData().getJails().get(jailName.toLowerCase(Locale.ENGLISH)).getStoredLocation();
			if (loc == null || loc.getWorld() == null)
			{
				throw new Exception(_("jailNotExist"));
			}
			return loc;
		}
		finally
		{
			unlock();
		}
	}

	@Override
	public Collection<String> getList() throws Exception
	{
		acquireReadLock();
		try
		{
			if (getData().getJails() == null)
			{
				return Collections.emptyList();
			}
			return new ArrayList<String>(getData().getJails().keySet());
		}
		finally
		{
			unlock();
		}
	}

	@Override
	public void removeJail(final String jail) throws Exception
	{
		acquireWriteLock();
		try
		{
			if (getData().getJails() == null)
			{
				return;
			}
			getData().getJails().remove(jail.toLowerCase(Locale.ENGLISH));
		}
		finally
		{
			unlock();
		}
	}

	@Override
	public void sendToJail(final IUser user, final String jail) throws Exception
	{
		acquireReadLock();
		try
		{
			if (user.isOnline())
			{
				Location loc = getJail(jail);
				user.getTeleport().now(loc, false, TeleportCause.COMMAND);
			}
			user.acquireWriteLock();
			try
			{
				user.getData().setJail(jail);
			}
			finally
			{
				unlock();
			}
		}
		finally
		{
			unlock();
		}
	}

	@Override
	public void setJail(final String jailName, final Location loc) throws Exception
	{
		acquireWriteLock();
		try
		{
			getData().addJail(jailName.toLowerCase(Locale.ENGLISH), loc);
		}
		finally
		{
			unlock();
		}
	}

	@Override
	public void finishRead()
	{
		
	}

	@Override
	public void finishWrite()
	{
		
	}

	@Override
	public int getCount()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}


	private class JailBlockListener implements Listener
	{
		@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
		public void onBlockBreak(final BlockBreakEvent event)
		{
			@Cleanup
			final IUser user = ess.getUserMap().getUser(event.getPlayer());
			user.acquireReadLock();
			if (user.getData().isJailed())
			{
				event.setCancelled(true);
			}
		}

		@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
		public void onBlockPlace(final BlockPlaceEvent event)
		{
			@Cleanup
			final IUser user = ess.getUserMap().getUser(event.getPlayer());
			user.acquireReadLock();
			if (user.getData().isJailed())
			{
				event.setCancelled(true);
			}
		}

		@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
		public void onBlockDamage(final BlockDamageEvent event)
		{
			@Cleanup
			final IUser user = ess.getUserMap().getUser(event.getPlayer());
			user.acquireReadLock();
			if (user.getData().isJailed())
			{
				event.setCancelled(true);
			}
		}
	}


	private class JailPlayerListener implements Listener
	{
		@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
		public void onPlayerInteract(final PlayerInteractEvent event)
		{
			@Cleanup
			final IUser user = ess.getUserMap().getUser(event.getPlayer());
			user.acquireReadLock();
			if (user.getData().isJailed())
			{
				event.setCancelled(true);
			}
		}

		@EventHandler(priority = EventPriority.HIGHEST)
		public void onPlayerRespawn(final PlayerRespawnEvent event)
		{
			@Cleanup
			final IUser user = ess.getUserMap().getUser(event.getPlayer());
			user.acquireReadLock();
			if (!user.getData().isJailed() || user.getData().getJail() == null || user.getData().getJail().isEmpty())
			{
				return;
			}

			try
			{
				event.setRespawnLocation(getJail(user.getData().getJail()));
			}
			catch (Exception ex)
			{
				if (ess.getSettings().isDebug())
				{
					LOGGER.log(Level.INFO, _("returnPlayerToJailError", user.getName(), ex.getLocalizedMessage()), ex);
				}
				else
				{
					LOGGER.log(Level.INFO, _("returnPlayerToJailError", user.getName(), ex.getLocalizedMessage()));
				}
			}
		}

		@EventHandler(priority = EventPriority.HIGH)
		public void onPlayerTeleport(final PlayerTeleportEvent event)
		{
			@Cleanup
			final IUser user = ess.getUserMap().getUser(event.getPlayer());
			user.acquireReadLock();
			if (!user.getData().isJailed() || user.getData().getJail() == null || user.getData().getJail().isEmpty())
			{
				return;
			}

			try
			{
				event.setTo(getJail(user.getData().getJail()));
			}
			catch (Exception ex)
			{
				if (ess.getSettings().isDebug())
				{
					LOGGER.log(Level.INFO, _("returnPlayerToJailError", user.getName(), ex.getLocalizedMessage()), ex);
				}
				else
				{
					LOGGER.log(Level.INFO, _("returnPlayerToJailError", user.getName(), ex.getLocalizedMessage()));
				}
			}
			user.sendMessage(_("jailMessage"));
		}

		@EventHandler(priority = EventPriority.HIGHEST)
		public void onPlayerJoin(final PlayerJoinEvent event)
		{
			@Cleanup
			final IUser user = ess.getUserMap().getUser(event.getPlayer());
			user.acquireReadLock();
			if (!user.getData().isJailed() || user.getData().getJail() == null || user.getData().getJail().isEmpty())
			{
				return;
			}

			try
			{
				sendToJail(user, user.getData().getJail());
			}
			catch (Exception ex)
			{
				if (ess.getSettings().isDebug())
				{
					LOGGER.log(Level.INFO, _("returnPlayerToJailError", user.getName(), ex.getLocalizedMessage()), ex);
				}
				else
				{
					LOGGER.log(Level.INFO, _("returnPlayerToJailError", user.getName(), ex.getLocalizedMessage()));
				}
			}
			user.sendMessage(_("jailMessage"));
		}
	}
}
