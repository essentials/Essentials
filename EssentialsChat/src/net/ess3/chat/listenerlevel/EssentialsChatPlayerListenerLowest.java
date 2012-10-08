package net.ess3.chat.listenerlevel;

import java.util.Map;
import net.ess3.api.IEssentials;
import net.ess3.api.IUser;
import net.ess3.chat.ChatStore;
import net.ess3.chat.EssentialsChatPlayer;
import org.bukkit.Server;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;


public class EssentialsChatPlayerListenerLowest extends EssentialsChatPlayer
{
	public EssentialsChatPlayerListenerLowest(final Server server,
											  final IEssentials ess,
											  final Map<AsyncPlayerChatEvent, ChatStore> chatStorage)
	{
		super(server, ess, chatStorage);
	}

	@EventHandler(priority = EventPriority.LOWEST)
	@Override
	public void onPlayerChat(final AsyncPlayerChatEvent event)
	{
		if (event.isCancelled())
		{
			return;
		}
		final IUser user = ess.getUserMap().getUser(event.getPlayer());
		if(user == null)
		{
			event.setCancelled(true);
			return;
		}
		final ChatStore chatStore = new ChatStore(ess, user, getChatType(event.getMessage()));
		setChatStore(event, chatStore);
		formatChat(event, chatStore);
	}
}