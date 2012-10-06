package net.ess3.ranks;

import java.text.MessageFormat;
import net.ess3.api.IEssentials;
import net.ess3.api.IRanks;
import net.ess3.api.ISettings;
import net.ess3.api.IUser;
import net.ess3.utils.FormatUtil;
import net.ess3.utils.Util;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.plugin.RegisteredServiceProvider;


public class VaultGroups implements IRanks
{
	private final IEssentials ess;

	public VaultGroups(final IEssentials ess)
	{
		this.ess = ess;
	}
	
	private <T> T getServiceProvider(Class<T> clazz) {
		RegisteredServiceProvider<T> provider = ess.getServer().getServicesManager().getRegistration(clazz);
		return provider.getProvider();
	}

	@Override
	public double getHealCooldown(IUser player)
	{
		Chat chat = getServiceProvider(Chat.class);
		return chat.getPlayerInfoDouble(player.getPlayer(), "healcooldown", 0);
	}

	@Override
	public double getTeleportCooldown(IUser player)
	{
		Chat chat = getServiceProvider(Chat.class);
		return chat.getPlayerInfoDouble(player.getPlayer(), "teleportcooldown", 0);
	}

	@Override
	public double getTeleportDelay(IUser player)
	{
		Chat chat = getServiceProvider(Chat.class);
		return chat.getPlayerInfoDouble(player.getPlayer(), "teleportdelay", 0);
	}

	@Override
	public String getPrefix(IUser player)
	{
		Chat chat = getServiceProvider(Chat.class);
		return chat.getPlayerPrefix(player.getPlayer());
	}

	@Override
	public String getSuffix(IUser player)
	{
		Chat chat = getServiceProvider(Chat.class);
		return chat.getPlayerSuffix(player.getPlayer());
	}

	@Override
	public int getHomeLimit(IUser player)
	{
		Chat chat = getServiceProvider(Chat.class);
		return chat.getPlayerInfoInteger(player.getPlayer(), "homes", 0);
	}

	@Override
	public MessageFormat getChatFormat(final IUser player)
	{
		String format = getRawChatFormat(player);
		format = FormatUtil.replaceFormat(format);
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
		Chat chat = getServiceProvider(Chat.class);
		String chatformat = chat.getPlayerInfoString(player.getPlayer(), "chatformat", "");
		if (chatformat != null && !chatformat.isEmpty())
		{
			return chatformat;
		}

		ISettings settings = ess.getSettings();
		return settings.getData().getChat().getDefaultFormat();
	}

	@Override
	public String getMainGroup(IUser player)
	{
		Chat chat = getServiceProvider(Chat.class);
		return chat.getPrimaryGroup(player.getPlayer().getPlayer());
	}

	@Override
	public boolean inGroup(IUser player, String groupname)
	{
		Chat chat = getServiceProvider(Chat.class);
		for (String group : chat.getPlayerGroups(player.getPlayer()))
		{
			if (group.equalsIgnoreCase(groupname))
			{
				return true;
			}
		}
		return false;
	}
}
