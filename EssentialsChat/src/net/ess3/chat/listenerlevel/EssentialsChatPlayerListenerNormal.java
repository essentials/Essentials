package net.ess3.chat.listenerlevel;

import java.util.Map;
import net.ess3.api.IEssentials;
import net.ess3.chat.ChatStore;
import net.ess3.chat.EssentialsChatPlayer;
import org.bukkit.Server;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;


public class EssentialsChatPlayerListenerNormal extends EssentialsChatPlayer
{
	public EssentialsChatPlayerListenerNormal(
			final Server server, final IEssentials ess, final Map<AsyncPlayerChatEvent, ChatStore> chatStorage)
	{
		super(server, ess, chatStorage);
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	@Override
	public void onPlayerChat(final AsyncPlayerChatEvent event)
	{
		final ChatStore chatStore = getChatStore(event);
		handleLocalChat(event, chatStore);
	}
}
