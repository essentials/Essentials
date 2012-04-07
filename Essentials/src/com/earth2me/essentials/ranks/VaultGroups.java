package com.earth2me.essentials.ranks;

import com.earth2me.essentials.utils.Util;
import com.earth2me.essentials.api.IEssentials;
import com.earth2me.essentials.api.IRanks;
import com.earth2me.essentials.api.ISettings;
import com.earth2me.essentials.api.IUser;
import java.text.MessageFormat;
import lombok.Cleanup;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.plugin.RegisteredServiceProvider;


public class VaultGroups implements IRanks
{
	private final IEssentials ess;

	public VaultGroups(final IEssentials ess)
	{
		this.ess = ess;
	}

	@Override
	public double getHealCooldown(IUser player)
	{
		Chat chat = ess.getServer().getServiceProvider(Chat.class);
		return chat.getPlayerInfoDouble(player.getBase(), "healcooldown", 0);
	}

	@Override
	public double getTeleportCooldown(IUser player)
	{
		Chat chat = ess.getServer().getServiceProvider(Chat.class);
		return chat.getPlayerInfoDouble(player.getBase(), "teleportcooldown", 0);
	}

	@Override
	public double getTeleportDelay(IUser player)
	{
		Chat chat = ess.getServer().getServiceProvider(Chat.class);
		return chat.getPlayerInfoDouble(player.getBase(), "teleportdelay", 0);
	}

	@Override
	public String getPrefix(IUser player)
	{
		Chat chat = ess.getServer().getServiceProvider(Chat.class);
		return chat.getPlayerPrefix(player.getBase());
	}

	@Override
	public String getSuffix(IUser player)
	{
		Chat chat = ess.getServer().getServiceProvider(Chat.class);
		return chat.getPlayerSuffix(player.getBase());
	}

	@Override
	public int getHomeLimit(IUser player)
	{
		Chat chat = ess.getServer().getServiceProvider(Chat.class);
		return chat.getPlayerInfoInteger(player.getBase(), "homes", 0);
	}

	@Override
	public MessageFormat getChatFormat(final IUser player)
	{
		String format = getRawChatFormat(player);
		format = Util.replaceColor(format);
		format = format.replace("{DISPLAYNAME}", "%1$s");
		format = format.replace("{GROUP}", "{0}");
		format = format.replace("{MESSAGE}", "%2$s");
		format = format.replace("{WORLDNAME}", "{1}");
		format = format.replace("{SHORTWORLDNAME}", "{2}");
		format = format.replaceAll("\\{(\\D*)\\}", "\\[$1\\]");
		MessageFormat mFormat = new MessageFormat(format);
		return mFormat;
	}

	private String getRawChatFormat(final IUser player)
	{
		Chat chat = ess.getServer().getServiceProvider(Chat.class);
		String chatformat = chat.getPlayerInfoString(player.getBase(), "chatformat", "");
		if (chatformat != null && !chatformat.isEmpty())
		{
			return chatformat;
		}

		@Cleanup
		ISettings settings = ess.getSettings();
		settings.acquireReadLock();
		return settings.getData().getChat().getDefaultFormat();
	}

	@Override
	public String getMainGroup(IUser player)
	{
		Chat chat = ess.getServer().getServiceProvider(Chat.class);
		return chat.getPrimaryGroup(player.getBase());
	}

	@Override
	public boolean inGroup(IUser player, String groupname)
	{
		Chat chat = ess.getServer().getServiceProvider(Chat.class);
		for (String group : chat.getPlayerGroups(player.getBase()))
		{
			if (group.equalsIgnoreCase(groupname))
			{
				return true;
			}
		}
		return false;
	}
}
