package com.earth2me.essentials.chat;

import com.earth2me.essentials.api.EssentialsPlugin;
import com.earth2me.essentials.chat.listenerlevel.EssentialsChatPlayerListenerHighest;
import com.earth2me.essentials.chat.listenerlevel.EssentialsChatPlayerListenerLowest;
import com.earth2me.essentials.chat.listenerlevel.EssentialsChatPlayerListenerNormal;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.plugin.PluginManager;


public class EssentialsChat extends EssentialsPlugin
{
	@Override
	public void onEnable()
	{
		// Always call FIRST to initialize context.
		super.onEnable();

		final PluginManager pluginManager = getServer().getPluginManager();

		final Map<PlayerChatEvent, ChatStore> chatStore = new HashMap<PlayerChatEvent, ChatStore>();

		final EssentialsChatPlayerListenerLowest playerListenerLowest = new EssentialsChatPlayerListenerLowest(getServer(), getContext(), chatStore);
		final EssentialsChatPlayerListenerNormal playerListenerNormal = new EssentialsChatPlayerListenerNormal(getServer(), getContext(), chatStore);
		final EssentialsChatPlayerListenerHighest playerListenerHighest = new EssentialsChatPlayerListenerHighest(getServer(), getContext(), chatStore);
		pluginManager.registerEvents(playerListenerLowest, this);
		pluginManager.registerEvents(playerListenerNormal, this);
		pluginManager.registerEvents(playerListenerHighest, this);

		final EssentialsLocalChatEventListener localChatListener = new EssentialsLocalChatEventListener(getServer(), getContext());
		pluginManager.registerEvents(localChatListener, this);
	}
}
