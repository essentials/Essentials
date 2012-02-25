package com.earth2me.essentials.geoip;

import com.earth2me.essentials.api.IContext;
import com.earth2me.essentials.api.IReloadable;
import static com.earth2me.essentials.components.i18n.I18nComponent._;
import com.earth2me.essentials.components.users.IUser;
import com.earth2me.essentials.perm.Permissions;
import com.maxmind.geoip.Location;
import com.maxmind.geoip.LookupService;
import com.maxmind.geoip.regionName;
import java.io.*;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;


public class EssentialsGeoIpPlayerListener implements Listener, IReloadable
{
	private transient LookupService ls = null;
	private static final Logger LOGGER = Logger.getLogger("Minecraft");
	private transient File databaseFile;
	private final transient GeoIpSettingsComponent settings;
	private final transient IContext context;
	private final transient EssentialsGeoIpPlugin plugin;

	public EssentialsGeoIpPlayerListener(final EssentialsGeoIpPlugin plugin, final IContext context)
	{
		super();
		this.context = context;
		this.plugin = plugin;
		this.settings = new GeoIpSettingsComponent(context, plugin);
		reload();
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerJoin(final PlayerJoinEvent event)
	{
		final IUser u = context.getUser(event.getPlayer());
		if (Permissions.GEOIP_HIDE.isAuthorized(u))
		{
			return;
		}

		settings.acquireReadLock();
		try
		{
			final InetAddress address = event.getPlayer().getAddress().getAddress();
			final StringBuilder builder = new StringBuilder();
			if (settings.getData().getDatabase().isShowCities())
			{
				final Location loc = ls.getLocation(address);
				if (loc == null)
				{
					return;
				}
				if (loc.city != null)
				{
					builder.append(loc.city).append(", ");
				}
				final String region = regionName.regionNameByCode(loc.countryCode, loc.region);
				if (region != null)
				{
					builder.append(region).append(", ");
				}
				builder.append(loc.countryName);
			}
			else
			{
				builder.append(ls.getCountry(address).getName());
			}
			if (settings.getData().isShowOnWhois())
			{
				u.acquireWriteLock();
				try
				{
					u.getData().setGeolocation(builder.toString());
				}
				finally
				{
					u.unlock();
				}
			}
			if (settings.getData().isShowOnLogin() && !u.isHidden())
			{
				for (Player player : event.getPlayer().getServer().getOnlinePlayers())
				{
					final IUser user = context.getUser(player);
					if (Permissions.GEOIP_SHOW.isAuthorized(user))
					{
						user.sendMessage(_("geoipJoinFormat", user.getDisplayName(), builder.toString()));
					}
				}
			}
		}
		finally
		{
			settings.unlock();
		}
	}

	@Override
	public final void reload()
	{
		settings.reload();
		settings.acquireReadLock();
		try
		{
			if (settings.getData().getDatabase().isShowCities())
			{
				databaseFile = new File(plugin.getDataFolder(), "GeoIPCity.dat");
			}
			else
			{
				databaseFile = new File(plugin.getDataFolder(), "GeoIP.dat");
			}
			if (!databaseFile.exists())
			{
				if (settings.getData().getDatabase().isDownloadIfMissing())
				{
					if (settings.getData().getDatabase().isShowCities())
					{
						downloadDatabase(settings.getData().getDatabase().getDownloadUrlCity());
					}
					else
					{
						downloadDatabase(settings.getData().getDatabase().getDownloadUrl());
					}
				}
				else
				{
					LOGGER.log(Level.SEVERE, _("cantFindGeoIpDB"));
					return;
				}
			}
			try
			{
				ls = new LookupService(databaseFile);
			}
			catch (IOException ex)
			{
				LOGGER.log(Level.SEVERE, _("cantReadGeoIpDB"), ex);
			}
		}
		finally
		{
			settings.unlock();
		}
	}

	private void downloadDatabase(final String url)
	{
		if (url == null || url.isEmpty())
		{
			LOGGER.log(Level.SEVERE, _("geoIpUrlEmpty"));
			return;
		}
		InputStream input = null;
		OutputStream output = null;
		try
		{
			LOGGER.log(Level.INFO, _("downloadingGeoIp"));
			final URL downloadUrl = new URL(url);
			final URLConnection conn = downloadUrl.openConnection();
			conn.setConnectTimeout(10000);
			conn.connect();
			input = conn.getInputStream();
			if (url.endsWith(".gz"))
			{
				input = new GZIPInputStream(input);
			}
			output = new FileOutputStream(databaseFile);
			final byte[] buffer = new byte[2048];
			int length = input.read(buffer);
			while (length >= 0)
			{
				output.write(buffer, 0, length);
				length = input.read(buffer);
			}
			input.close();
			output.close();
		}
		catch (MalformedURLException ex)
		{
			LOGGER.log(Level.SEVERE, _("geoIpUrlInvalid"), ex);
		}
		catch (IOException ex)
		{
			LOGGER.log(Level.SEVERE, _("connectionFailed"), ex);
		}
		finally
		{
			if (output != null)
			{
				try
				{
					output.close();
				}
				catch (IOException ex)
				{
					LOGGER.log(Level.SEVERE, _("connectionFailed"), ex);
				}
			}
			if (input != null)
			{
				try
				{
					input.close();
				}
				catch (Throwable ex)
				{
					LOGGER.log(Level.SEVERE, _("connectionFailed"), ex);
				}
			}
		}
	}
}
