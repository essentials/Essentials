package net.ess3.geoip;

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
import static net.ess3.I18n._;
import net.ess3.api.IEssentials;
import net.ess3.api.IReload;
import net.ess3.api.IUser;
import net.ess3.permissions.Permissions;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;


public class EssentialsGeoIPPlayerListener implements Listener, IReload
{
	private LookupService ls = null;
	private File databaseFile;
	private final ConfigHolder config;
	private final IEssentials ess;
	private final Plugin geoip;

	public EssentialsGeoIPPlayerListener(final Plugin geoip, final IEssentials ess)
	{
		super();
		this.ess = ess;
		this.geoip = geoip;
		this.config = new ConfigHolder(ess, geoip);
		onReload();
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerJoin(final PlayerJoinEvent event)
	{
		if (Permissions.GEOIP_HIDE.isAuthorized(event.getPlayer()))
		{
			return;
		}
		if (event.getPlayer().getAddress() == null || event.getPlayer().getAddress().getAddress() == null)
		{
			return;
		}
		final InetAddress address = event.getPlayer().getAddress().getAddress();

		final StringBuilder builder = new StringBuilder();
		if (config.getData().getDatabase().isShowCities())
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
		if (config.getData().isShowOnWhois())
		{
			final IUser u = ess.getUserMap().getUser(event.getPlayer());
			u.getData().setGeolocation(builder.toString());
			u.queueSave();
		}
		if (config.getData().isShowOnLogin())
		{
			for (Player player : event.getPlayer().getServer().getOnlinePlayers())
			{
				if (!player.canSee(event.getPlayer()))
				{
					continue;
				}
				if (Permissions.GEOIP_SHOW.isAuthorized(player))
				{
					player.sendMessage(_("§6Player §c{0} §6comes from §c{1}§6.", player.getDisplayName(), builder.toString()));
				}
			}
		}
	}

	@Override
	public final void onReload()
	{
		config.onReload();
		if (config.getData().getDatabase().isShowCities())
		{
			databaseFile = new File(geoip.getDataFolder(), "GeoIPCity.dat");
		}
		else
		{
			databaseFile = new File(geoip.getDataFolder(), "GeoIP.dat");
		}
		if (!databaseFile.exists())
		{
			if (config.getData().getDatabase().isDownloadIfMissing())
			{
				if (config.getData().getDatabase().isShowCities())
				{
					downloadDatabase(config.getData().getDatabase().getDownloadUrlCity());
				}
				else
				{
					downloadDatabase(config.getData().getDatabase().getDownloadUrl());
				}
			}
			else
			{
				ess.getLogger().log(Level.SEVERE, _("Can't find GeoIP database!"));
				return;
			}
		}
		try
		{
			ls = new LookupService(databaseFile);
		}
		catch (IOException ex)
		{
			ess.getLogger().log(Level.SEVERE, _("Failed to read GeoIP database!"), ex);
		}
	}

	private void downloadDatabase(final String url)
	{
		if (url == null || url.isEmpty())
		{
			ess.getLogger().log(Level.SEVERE, _("GeoIP download url is empty."));
			return;
		}
		if (!databaseFile.getAbsoluteFile().getParentFile().exists())
		{
			databaseFile.getAbsoluteFile().getParentFile().mkdirs();
		}
		InputStream input = null;
		OutputStream output = null;
		try
		{
			ess.getLogger().log(Level.INFO, _("Downloading GeoIP database... this might take a while (country: 0.6 MB, city: 20MB)"));
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
			ess.getLogger().log(Level.SEVERE, _("GeoIP download url is invalid."), ex);
		}
		catch (IOException ex)
		{
			ess.getLogger().log(Level.SEVERE, _("Failed to open connection."), ex);
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
					ess.getLogger().log(Level.SEVERE, _("Failed to open connection."), ex);
				}
			}
			if (input != null)
			{
				try
				{
					input.close();
				}
				catch (IOException ex)
				{
					ess.getLogger().log(Level.SEVERE, _("Failed to open connection."), ex);
				}
			}
		}
	}
}
