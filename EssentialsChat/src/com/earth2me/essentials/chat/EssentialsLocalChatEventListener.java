package com.earth2me.essentials.chat;

import static com.earth2me.essentials.I18n._;
import com.earth2me.essentials.api.IEssentials;
import com.earth2me.essentials.api.IUser;
import com.earth2me.essentials.permissions.Permissions;
import java.util.logging.Logger;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;


public class EssentialsLocalChatEventListener implements Listener
{
	protected transient IEssentials ess;
	protected final transient Server server;
	private static final Logger LOGGER = Logger.getLogger("Minecraft");

	public EssentialsLocalChatEventListener(final Server server, final IEssentials ess)
	{
		this.ess = ess;
		this.server = server;
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onLocalChat(final EssentialsLocalChatEvent event)
	{
		final Player sender = event.getPlayer();
		final Location loc = sender.getLocation();
		final World world = loc.getWorld();

		for (Player onlinePlayer : server.getOnlinePlayers())
		{
			String type = _("chatTypeLocal");
			final IUser user = ess.getUser(onlinePlayer);
			//TODO: remove reference to op 
			if (user.isIgnoringPlayer(sender.getName()) && !sender.isOp())
			{
				continue;
			}
			if (!user.equals(sender))
			{
				if (Permissions.CHAT_SPY.isAuthorized(user))
				{
					type = type.concat(_("chatTypeSpy"));
				}
				else
				{
					final Location playerLoc = user.getLocation();
					if (playerLoc.getWorld() != world)
					{
						continue;
					}
					final double delta = playerLoc.distanceSquared(loc);

					if (delta > event.getRadius())
					{
						continue;
					}
				}

				final String message = type.concat(String.format(event.getFormat(), sender.getDisplayName(), event.getMessage()));
				user.sendMessage(message);
			}
		}
	
	}
}