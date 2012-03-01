package com.earth2me.essentials.components.settings.jails;

import com.earth2me.essentials.api.IContext;
import com.earth2me.essentials.api.IEssentials;
import static com.earth2me.essentials.components.i18n.I18nComponent._;
import com.earth2me.essentials.components.users.IUserComponent;
import com.earth2me.essentials.storage.LocationData;
import com.earth2me.essentials.storage.StorageComponent;
import java.util.*;
import java.util.logging.Level;
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


public final class JailsComponent extends StorageComponent<Jails, IEssentials> implements IJailsComponent
{
	public JailsComponent(final IContext context, final IEssentials plugin)
	{
		super(context, Jails.class, plugin);
	}

	@Override
	public String getContainerId()
	{
		return "jail-database";
	}

	@Override
	public void onEnable()
	{
		super.onEnable();

		registerListeners();
	}

	private void registerListeners()
	{
		final PluginManager pluginManager = getContext().getServer().getPluginManager();

		pluginManager.registerEvents(new JailBlockListener(), getContext().getEssentials());
		pluginManager.registerEvents(new JailPlayerListener(), getContext().getEssentials());
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
	public void sendToJail(final IUserComponent user, final String jail) throws Exception
	{
		acquireReadLock();
		if (!(user.isOnline()))
		{
			Location loc = null;
			try
			{
				loc = getJail(jail);
			}
			finally
			{
				unlock();
			}
			if (loc != null)
			{
				user.getTeleporter().now(loc, false, TeleportCause.COMMAND);
			}
		}

		user.setJail(jail);
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
			if (getContext().getUser(event.getPlayer()).isJailed())
			{
				event.setCancelled(true);
			}
		}

		@EventHandler(priority = EventPriority.LOW)
		public void onBlockPlace(final BlockPlaceEvent event)
		{
			if (getContext().getUser(event.getPlayer()).isJailed())
			{
				event.setCancelled(true);
			}
		}

		@EventHandler(priority = EventPriority.LOW)
		public void onBlockDamage(final BlockDamageEvent event)
		{
			if (getContext().getUser(event.getPlayer()).isJailed())
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
			if (getContext().getUser(event.getPlayer()).isJailed())
			{
				event.setCancelled(true);
			}
		}

		@EventHandler(priority = EventPriority.HIGH)
		public void onPlayerRespawn(final PlayerRespawnEvent event)
		{
			final IUserComponent user = getContext().getUser(event.getPlayer());
			if (!user.isJailed() || user.getJail() == null || user.getJail().isEmpty())
			{
				return;
			}

			try
			{
				event.setRespawnLocation(getJail(user.getJail()));
			}
			catch (Exception ex)
			{
				if (getContext().getSettings().isDebug())
				{
					getContext().getLogger().log(Level.INFO, _("returnPlayerToJailError", user.getName(), ex.getLocalizedMessage()), ex);
				}
				else
				{
					getContext().getLogger().log(Level.INFO, _("returnPlayerToJailError", user.getName(), ex.getLocalizedMessage()));
				}
			}
		}

		@EventHandler(priority = EventPriority.HIGH)
		public void onPlayerTeleport(final PlayerTeleportEvent event)
		{
			final IUserComponent user = getContext().getUser(event.getPlayer());
			if (!user.isJailed() || user.getJail() == null || user.getJail().isEmpty())
			{
				return;
			}

			try
			{
				event.setTo(getJail(user.getJail()));
			}
			catch (Exception ex)
			{
				if (getContext().getSettings().isDebug())
				{
					getContext().getLogger().log(Level.INFO, _("returnPlayerToJailError", user.getName(), ex.getLocalizedMessage()), ex);
				}
				else
				{
					getContext().getLogger().log(Level.INFO, _("returnPlayerToJailError", user.getName(), ex.getLocalizedMessage()));
				}
			}
			user.sendMessage(_("jailMessage"));
		}

		@EventHandler(priority = EventPriority.HIGH)
		public void onPlayerJoin(final PlayerJoinEvent event)
		{
			final IUserComponent user = getContext().getUser(event.getPlayer());
			if (!user.isJailed() || user.getJail() == null || user.getJail().isEmpty())
			{
				return;
			}

			try
			{
				sendToJail(user, user.getJail());
			}
			catch (Exception ex)
			{
				if (getContext().getSettings().isDebug())
				{
					getContext().getLogger().log(Level.INFO, _("returnPlayerToJailError", user.getName(), ex.getLocalizedMessage()), ex);
				}
				else
				{
					getContext().getLogger().log(Level.INFO, _("returnPlayerToJailError", user.getName(), ex.getLocalizedMessage()));
				}
			}
			user.sendMessage(_("jailMessage"));
		}
	}
}
