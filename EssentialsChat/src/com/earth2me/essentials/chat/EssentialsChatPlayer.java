package com.earth2me.essentials.chat;

import com.earth2me.essentials.ChargeException;
import com.earth2me.essentials.Trade;
import com.earth2me.essentials.User;
import net.ess3.api.IEssentials;
import java.util.Map;
import java.util.logging.Logger;
import org.bukkit.Server;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public abstract class EssentialsChatPlayer implements Listener
{
	protected transient IEssentials ess;
	protected final static Logger logger = Logger.getLogger("Minecraft");
	protected final transient Server server;
	protected final transient Map<AsyncPlayerChatEvent, ChatStore> chatStorage;

	public EssentialsChatPlayer(final Server server,
								final IEssentials ess,
								final Map<AsyncPlayerChatEvent, ChatStore> chatStorage)
	{
		this.ess = ess;
		this.server = server;
		this.chatStorage = chatStorage;
	}

	public void onPlayerChat(final AsyncPlayerChatEvent event)
	{
	}

	public boolean isAborted(final AsyncPlayerChatEvent event)
	{
		if (event.isCancelled())
		{
			return true;
		}
		return false;
	}

	public String getChatType(final String message)
	{
		switch (message.charAt(0))
		{
		case '!':
			return "shout";
		case '?':
			return "question";
		//case '@':
		//return "admin";
		default:
			return "";
		}
	}

	public ChatStore getChatStore(final AsyncPlayerChatEvent event)
	{
		return chatStorage.get(event);
	}

	public void setChatStore(final AsyncPlayerChatEvent event, final ChatStore chatStore)
	{
		chatStorage.put(event, chatStore);
	}

	public ChatStore delChatStore(final AsyncPlayerChatEvent event)
	{
		return chatStorage.remove(event);
	}

	protected void charge(final User user, final Trade charge) throws ChargeException
	{
		charge.charge(user);
	}

	protected boolean charge(final AsyncPlayerChatEvent event, final ChatStore chatStore)
	{
		try
		{
			charge(chatStore.getUser(), chatStore.getCharge());
		}
		catch (ChargeException e)
		{
			ess.showError(chatStore.getUser().getSource(), e, chatStore.getLongType());
			event.setCancelled(true);
			return false;
		}
		return true;
	}
}
