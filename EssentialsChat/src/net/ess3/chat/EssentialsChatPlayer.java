package net.ess3.chat;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Logger;
import static net.ess3.I18n._;
import net.ess3.api.*;
import net.ess3.economy.Trade;
import net.ess3.permissions.Permissions;
import net.ess3.utils.FormatUtil;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;


public abstract class EssentialsChatPlayer implements Listener
{
	protected IEssentials ess;
	protected final Server server;
	protected final Map<AsyncPlayerChatEvent, ChatStore> chatStorage;

	public EssentialsChatPlayer(
			final Server server, final IEssentials ess, final Map<AsyncPlayerChatEvent, ChatStore> chatStorage)
	{
		this.ess = ess;
		this.server = server;
		this.chatStorage = chatStorage;
	}

	public void onPlayerChat(final AsyncPlayerChatEvent event)
	{
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

	protected void chargeChat(final AsyncPlayerChatEvent event, final ChatStore chatStore)
	{
		try
		{
			charge(chatStore.getUser(), chatStore.getCharge());
		}
		catch (ChargeException e)
		{
			ess.getCommandHandler().showCommandError(chatStore.getUser(), chatStore.getLongType(), e);
			event.setCancelled(true);
		}
	}

	protected void charge(final CommandSender sender, final Trade charge) throws ChargeException
	{
		if (sender instanceof IUser)
		{
			charge.charge((IUser)sender);
		}
	}

	protected void formatChat(final AsyncPlayerChatEvent event, final ChatStore chatStore)
	{
		final IUser user = chatStore.getUser();
		event.setMessage(FormatUtil.formatMessage(user, Permissions.CHAT, event.getMessage()));
		String group = ess.getRanks().getMainGroup(user);
		String world = user.getPlayer().getWorld().getName();

		IRanks groupSettings = ess.getRanks();
		MessageFormat format = groupSettings.getChatFormat(user);
		synchronized (format)
		{
			event.setFormat(
					format.format(
					new Object[]
					{
						group, world, world.substring(0, 1).toUpperCase(Locale.ENGLISH)
					}));
		}
	}

	//TODO: Flesh this out - '?' trigger is too easily accidentally triggered
	protected String getChatType(final String message)
	{
		switch (message.charAt(0))
		{
		case '!':
			return "shout";
		//case '?':
		//return "question";
		//case '@':
		//	return "admin";			
		default:
			return "";
		}
	}

	protected void handleLocalChat(final AsyncPlayerChatEvent event, final ChatStore chatStore)
	{
		ISettings settings = ess.getSettings();
		long radius = settings.getData().getChat().getLocalRadius();

		if (radius < 1)
		{
			return;
		}

		radius *= radius;

		final IUser user = chatStore.getUser();

		if (event.getMessage().length() > 1 && chatStore.getType().length() > 0)
		{
			if (ChatPermissions.getPermission(chatStore.getType()).isAuthorized(user))
			{
				final StringBuilder format = new StringBuilder();
				format.append(chatStore.getType()).append("Format");
				event.setMessage(event.getMessage().substring(1));
				event.setFormat(_(format.toString(), event.getFormat()));
				return;
			}

			final StringBuilder errorMsg = new StringBuilder();
			errorMsg.append("notAllowedTo").append(chatStore.getType().substring(0, 1).toUpperCase(Locale.ENGLISH)).append(chatStore.getType().substring(1));

			user.sendMessage(_(errorMsg.toString()));
			event.setCancelled(true);
			return;
		}

		event.setCancelled(true);
		final EssentialsLocalChatEvent localChat = new EssentialsLocalChatEvent(event, radius);
		ess.getServer().getPluginManager().callEvent(localChat);
	}
}
