package com.earth2me.essentials.chat;

import com.earth2me.essentials.IEssentials;
import com.earth2me.essentials.User;
import com.earth2me.essentials.Util;
import java.util.Map;
import java.util.logging.Logger;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerListener;


public class EssentialsChatPlayerListener extends PlayerListener
{
	private static final Logger LOGGER = Logger.getLogger("Minecraft");
	private final transient IEssentials ess;
	private final transient Server server;
	private final transient Map<String, IEssentialsChatListener> listeners;

	public EssentialsChatPlayerListener(final Server server, final IEssentials ess, final Map<String, IEssentialsChatListener> listeners)
	{
		this.server = server;
		this.ess = ess;
		this.listeners = listeners;
	}

	@Override
	public void onPlayerChat(final PlayerChatEvent event)
	{
		if (event.isCancelled())
		{
			return;
		}

		for (IEssentialsChatListener listener : listeners.values())
		{
			if (listener.shouldHandleThisChat(event))
			{
				return;
			}
		}

		final User user = ess.getUser(event.getPlayer());

		if (user.isAuthorized("essentials.chat.color"))
		{
			event.setMessage(event.getMessage().replaceAll("&([0-9a-f])", "§$1"));
		}

		event.setFormat(ess.getSettings().getChatFormat(user.getGroup()).replace('&', '§').replace("§§", "&").replace("{DISPLAYNAME}", "%1$s").replace("{GROUP}", user.getGroup()).replace("{MESSAGE}", "%2$s").replace("{WORLDNAME}", user.getWorld().getName()).replace("{SHORTWORLDNAME}", user.getWorld().getName().substring(0, 1).toUpperCase()));

		final int radius = ess.getSettings().getChatRadius();
		if (radius < 1)
		{
			return;
		}

		if (event.getMessage().startsWith("!") && event.getMessage().length() > 1)
		{
			if (user.isAuthorized("essentials.chat.shout"))
			{
				event.setMessage(event.getMessage().substring(1));
				event.setFormat(Util.format("shoutFormat", event.getFormat()));
				return;
			}
			user.sendMessage(Util.i18n("notAllowedToShout"));
			event.setCancelled(true);
			return;
		}

		if (event.getMessage().startsWith("?") && event.getMessage().length() > 1)
		{
			if (user.isAuthorized("essentials.chat.question"))
			{
				event.setMessage(event.getMessage().substring(1));
				event.setFormat(Util.format("questionFormat", event.getFormat()));
				return;
			}
			user.sendMessage(Util.i18n("notAllowedToQuestion"));
			event.setCancelled(true);
			return;
		}

		event.setCancelled(true);
		LOGGER.info(Util.format("localFormat", user.getName(), event.getMessage()));

		final Location loc = user.getLocation();
		final World world = loc.getWorld();
		final int x = loc.getBlockX();
		final int y = loc.getBlockY();
		final int z = loc.getBlockZ();

		for (Player p : server.getOnlinePlayers())
		{
			final User u = ess.getUser(p);
			if (u.isIgnoredPlayer(user.getName()) && !user.isOp())
			{
				continue;
			}
			if (!u.equals(user) && !u.isAuthorized("essentials.chat.spy"))
			{
				final Location l = u.getLocation();
				final int dx = Math.abs(x - l.getBlockX());
				final int dy = Math.abs(y - l.getBlockY());
				final int dz = Math.abs(z - l.getBlockZ());
				final int delta = dx + dy + dz;
				if (delta > radius || world != l.getWorld())
				{
					continue;
				}
			}
			String message = String.format(event.getFormat(), user.getDisplayName(), event.getMessage());

			for (IEssentialsChatListener listener : listeners.values())
			{
				message = listener.modifyMessage(event, p, message);
			}

			u.sendMessage(message);
		}
	}
}
