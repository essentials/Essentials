package com.earth2me.essentials.anticheat.checks.chat;

import com.earth2me.essentials.anticheat.EventManager;
import com.earth2me.essentials.anticheat.NoCheat;
import com.earth2me.essentials.anticheat.NoCheatPlayer;
import com.earth2me.essentials.anticheat.config.ConfigurationCacheStore;
import com.earth2me.essentials.anticheat.config.Permissions;
import java.util.LinkedList;
import java.util.List;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;


/**
 * Central location to listen to events that are relevant for the chat checks
 *
 */
public class ChatCheckListener implements Listener, EventManager
{
	private final SpamCheck spamCheck;
	private final ColorCheck colorCheck;
	private final NoCheat plugin;

	public ChatCheckListener(NoCheat plugin)
	{

		this.plugin = plugin;

		spamCheck = new SpamCheck(plugin);
		colorCheck = new ColorCheck(plugin);
	}

	/**
	 * We listen to PlayerCommandPreprocess events because commands can be used for spamming too.
	 *
	 * @param event The PlayerCommandPreprocess Event
	 */
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void commandPreprocess(final PlayerCommandPreprocessEvent event)
	{
		// This type of event is derived from PlayerChatEvent, therefore
		// just treat it like that
		chat(event);
	}

	/**
	 * We listen to PlayerChat events for obvious reasons
	 *
	 * @param event The PlayerChat event
	 */
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void chat(final PlayerChatEvent event)
	{
		boolean cancelled = false;

		final NoCheatPlayer player = plugin.getPlayer(event.getPlayer());
		final ChatConfig cc = ChatCheck.getConfig(player);
		final ChatData data = ChatCheck.getData(player);

		// Remember the original message
		data.message = event.getMessage();

		// Now do the actual checks

		// First the spam check
		if (cc.spamCheck && !player.hasPermission(Permissions.CHAT_SPAM))
		{
			cancelled = spamCheck.check(player, data, cc);
		}

		// Second the color check
		if (!cancelled && cc.colorCheck && !player.hasPermission(Permissions.CHAT_COLOR))
		{
			cancelled = colorCheck.check(player, data, cc);
		}

		// If one of the checks requested the event to be cancelled, do it
		if (cancelled)
		{
			event.setCancelled(cancelled);
		}
		else
		{
			// In case one of the events modified the message, make sure that
			// the new message gets used
			event.setMessage(data.message);
		}
	}

	public List<String> getActiveChecks(ConfigurationCacheStore cc)
	{
		LinkedList<String> s = new LinkedList<String>();

		ChatConfig c = ChatCheck.getConfig(cc);
		if (c.spamCheck)
		{
			s.add("chat.spam");
		}
		if (c.colorCheck)
		{
			s.add("chat.color");
		}
		return s;
	}
}
