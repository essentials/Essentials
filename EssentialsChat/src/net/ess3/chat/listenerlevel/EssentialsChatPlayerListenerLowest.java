package net.ess3.chat.listenerlevel;

import net.ess3.api.IEssentials;
import net.ess3.api.IUser;
import net.ess3.chat.ChatStore;
import net.ess3.chat.EssentialsChatPlayer;
import java.util.Map;

import net.ess3.chat.EssentialsChatPlayer;
import org.bukkit.Server;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerChatEvent;


public class EssentialsChatPlayerListenerLowest extends EssentialsChatPlayer
{
	public EssentialsChatPlayerListenerLowest(final Server server,
											  final IEssentials ess,
											  final Map<PlayerChatEvent, ChatStore> chatStorage)
	{
		super(server, ess, chatStorage);
	}

	@EventHandler(priority = EventPriority.LOWEST)
	@Override
	public void onPlayerChat(final PlayerChatEvent event)
	{
		if (event.isCancelled())
		{
			return;
		}
		final IUser user = ess.getUser(event.getPlayer());
		final ChatStore chatStore = new ChatStore(ess, user, getChatType(event.getMessage()));
		setChatStore(event, chatStore);
		formatChat(event, chatStore);
	}
}