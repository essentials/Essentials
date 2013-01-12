package net.ess3.chat;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import static net.ess3.I18n._;
import net.ess3.api.IEssentials;
import net.ess3.api.IPlugin;
import net.ess3.chat.listenerlevel.EssentialsChatPlayerListenerHighest;
import net.ess3.chat.listenerlevel.EssentialsChatPlayerListenerLowest;
import net.ess3.chat.listenerlevel.EssentialsChatPlayerListenerNormal;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;


public class EssentialsChat extends JavaPlugin
{
	private static final Logger LOGGER = Logger.getLogger("Minecraft");

	@Override
	public void onEnable()
	{
		final PluginManager pluginManager = getServer().getPluginManager();
		final IPlugin plugin = (IPlugin)pluginManager.getPlugin("Essentials-3");
		final IEssentials ess = (IEssentials)plugin.getEssentials();
		if (!this.getDescription().getVersion().equals(plugin.getDescription().getVersion()))
		{
			LOGGER.log(Level.WARNING, _("versionMismatchAll"));
		}
		if (!plugin.isEnabled())
		{
			this.setEnabled(false);
			return;
		}

		final Map<AsyncPlayerChatEvent, ChatStore> chatStore = Collections.synchronizedMap(new HashMap<AsyncPlayerChatEvent, ChatStore>());

		final EssentialsChatPlayerListenerLowest playerListenerLowest = new EssentialsChatPlayerListenerLowest(getServer(), ess, chatStore);
		final EssentialsChatPlayerListenerNormal playerListenerNormal = new EssentialsChatPlayerListenerNormal(getServer(), ess, chatStore);
		final EssentialsChatPlayerListenerHighest playerListenerHighest = new EssentialsChatPlayerListenerHighest(getServer(), ess, chatStore);
		pluginManager.registerEvents(playerListenerLowest, this);
		pluginManager.registerEvents(playerListenerNormal, this);
		pluginManager.registerEvents(playerListenerHighest, this);

		final EssentialsLocalChatEventListener localChatListener = new EssentialsLocalChatEventListener(getServer(), ess);
		pluginManager.registerEvents(localChatListener, this);
	}
}
