package com.earth2me.essentials.components.jails;

import static com.earth2me.essentials.components.i18n.I18nComponent._;
import com.earth2me.essentials.api.IContext;
import com.earth2me.essentials.components.users.IUser;
import com.earth2me.essentials.components.settings.Jails;
import com.earth2me.essentials.storage.AsyncStorageObjectHolder;
import com.earth2me.essentials.storage.LocationData;
import java.io.File;
import java.util.*;
import java.util.logging.Level;
import lombok.Cleanup;
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


public class JailsComponent extends AsyncStorageObjectHolder<Jails> implements IJailsComponent
{
	public JailsComponent(final IContext ess)
	{
		super(ess, Jails.class);
	}

	@Override
	public String getTypeId()
	{
		return "JailsComponent";
	}

	@Override
	public void initialize()
	{
	}

	@Override
	public void onEnable()
	{
		reload();
		registerListeners();
	}

	private void registerListeners()
	{
		final PluginManager pluginManager = context.getServer().getPluginManager();

		pluginManager.registerEvents(new JailBlockListener(), context.getEssentials());
		pluginManager.registerEvents(new JailPlayerListener(), context.getEssentials());
	}

	@Override
	public File getStorageFile()
	{
		return new File(context.getDataFolder(), "jail.yml");
	}

	@Override
	public Location getJail(final String jailName) throws Exception
	{
		acquireReadLock();
		try
		{
			if (getData().getJails() == null
				|| jailName == null
				|| !getData().getJails().containsKey(jailName.toLowerCase(Locale.ENGLISH)))
			{
				throw new Exception(_("jailNotExist"));
			}

			Location loc = getData().getJails().get(jailName.toLowerCase(Locale.ENGLISH)).getBukkitLocation();
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
			final Map<String, LocationData> jails = getData().getJails();
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
			if (!(user.isOnline()))
			{
				final Location loc = getJail(jail);
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
			if (getData().getJails() == null)
			{
				getData().setJails(new HashMap<String, LocationData>());
			}
			getData().getJails().put(jailName.toLowerCase(Locale.ENGLISH), new LocationData(loc));
		}
		finally
		{
			unlock();
		}
	}


	private class JailBlockListener implements Listener
	{
		@EventHandler(priority = EventPriority.LOW)
		public void onBlockBreak(final BlockBreakEvent event)
		{
			@Cleanup
			final IUser user = context.getUser(event.getPlayer());
			user.acquireReadLock();
			if (user.getData().isJailed())
			{
				event.setCancelled(true);
			}
		}

		@EventHandler(priority = EventPriority.LOW)
		public void onBlockPlace(final BlockPlaceEvent event)
		{
			@Cleanup
			final IUser user = context.getUser(event.getPlayer());
			user.acquireReadLock();
			if (user.getData().isJailed())
			{
				event.setCancelled(true);
			}
		}

		@EventHandler(priority = EventPriority.LOW)
		public void onBlockDamage(final BlockDamageEvent event)
		{
			@Cleanup
			final IUser user = context.getUser(event.getPlayer());
			user.acquireReadLock();
			if (user.getData().isJailed())
			{
				event.setCancelled(true);
			}
		}
	}


	private class JailPlayerListener implements Listener
	{
		@EventHandler(priority = EventPriority.LOW)
		public void onPlayerInteract(final PlayerInteractEvent event)
		{
			@Cleanup
			final IUser user = context.getUser(event.getPlayer());
			user.acquireReadLock();
			if (user.getData().isJailed())
			{
				event.setCancelled(true);
			}
		}

		@EventHandler(priority = EventPriority.HIGH)
		public void onPlayerRespawn(final PlayerRespawnEvent event)
		{
			@Cleanup
			final IUser user = context.getUser(event.getPlayer());
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
				if (context.getSettings().isDebug())
				{
					context.getLogger().log(Level.INFO, _("returnPlayerToJailError", user.getName(), ex.getLocalizedMessage()), ex);
				}
				else
				{
					context.getLogger().log(Level.INFO, _("returnPlayerToJailError", user.getName(), ex.getLocalizedMessage()));
				}
			}
		}

		@EventHandler(priority = EventPriority.HIGH)
		public void onPlayerTeleport(final PlayerTeleportEvent event)
		{
			@Cleanup
			final IUser user = context.getUser(event.getPlayer());
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
				if (context.getSettings().isDebug())
				{
					context.getLogger().log(Level.INFO, _("returnPlayerToJailError", user.getName(), ex.getLocalizedMessage()), ex);
				}
				else
				{
					context.getLogger().log(Level.INFO, _("returnPlayerToJailError", user.getName(), ex.getLocalizedMessage()));
				}
			}
			user.sendMessage(_("jailMessage"));
		}

		@EventHandler(priority = EventPriority.HIGH)
		public void onPlayerJoin(final PlayerJoinEvent event)
		{
			@Cleanup
			final IUser user = context.getUser(event.getPlayer());
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
				if (context.getSettings().isDebug())
				{
					context.getLogger().log(Level.INFO, _("returnPlayerToJailError", user.getName(), ex.getLocalizedMessage()), ex);
				}
				else
				{
					context.getLogger().log(Level.INFO, _("returnPlayerToJailError", user.getName(), ex.getLocalizedMessage()));
				}
			}
			user.sendMessage(_("jailMessage"));
		}
	}
}
