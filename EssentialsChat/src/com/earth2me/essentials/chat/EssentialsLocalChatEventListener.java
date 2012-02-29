package com.earth2me.essentials.chat;

import static com.earth2me.essentials.components.i18n.I18nComponent.$;
import com.earth2me.essentials.api.IContext;
import com.earth2me.essentials.components.users.IUserComponent;
import com.earth2me.essentials.perm.Permissions;
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
	protected transient IContext ess;
	protected final transient Server server;
	private static final Logger LOGGER = Logger.getLogger("Minecraft");

	public EssentialsLocalChatEventListener(final Server server, final IContext ess)
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
			String type = $("chatTypeLocal");
			final IUserComponent user = ess.getUser(onlinePlayer);
			//TODO: remove reference to op
			if (user.isIgnoringPlayer(sender.getName()) && !sender.isOp())
			{
				continue;
			}
			if (!user.equals(sender))
			{
				final Location playerLoc = user.getLocation();
				if (playerLoc.getWorld() != world)
				{
					continue;
				}
				final double delta = playerLoc.distanceSquared(loc);

				if (delta > event.getRadius())
				{
					if (Permissions.CHAT_SPY.isAuthorized(user))
					{
						type = type.concat($("chatTypeSpy"));
					}
					else
					{
						continue;
					}
				}
			}

			final String message = type.concat(String.format(event.getFormat(), sender.getDisplayName(), event.getMessage()));
			user.sendMessage(message);
		}
	}
}