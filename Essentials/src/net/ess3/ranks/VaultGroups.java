package net.ess3.ranks;

import java.text.MessageFormat;
import lombok.Cleanup;
import net.ess3.api.IEssentials;
import net.ess3.api.IRanks;
import net.ess3.api.ISettings;
import net.ess3.api.IUser;
import net.ess3.bukkit.BukkitPlayer;
import net.ess3.utils.Util;
import net.milkbowl.vault.chat.Chat;


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
		return chat.getPlayerInfoDouble(((BukkitPlayer)player.getBase()).getPlayer(), "healcooldown", 0);
	}

	@Override
	public double getTeleportCooldown(IUser player)
	{
		Chat chat = ess.getServer().getServiceProvider(Chat.class);
		return chat.getPlayerInfoDouble(((BukkitPlayer)player.getBase()).getPlayer(), "teleportcooldown", 0);
	}

	@Override
	public double getTeleportDelay(IUser player)
	{
		Chat chat = ess.getServer().getServiceProvider(Chat.class);
		return chat.getPlayerInfoDouble(((BukkitPlayer)player.getBase()).getPlayer(), "teleportdelay", 0);
	}

	@Override
	public String getPrefix(IUser player)
	{
		Chat chat = ess.getServer().getServiceProvider(Chat.class);
		return chat.getPlayerPrefix(((BukkitPlayer)player.getBase()).getPlayer());
	}

	@Override
	public String getSuffix(IUser player)
	{
		Chat chat = ess.getServer().getServiceProvider(Chat.class);
		return chat.getPlayerSuffix(((BukkitPlayer)player.getBase()).getPlayer());
	}

	@Override
	public int getHomeLimit(IUser player)
	{
		Chat chat = ess.getServer().getServiceProvider(Chat.class);
		return chat.getPlayerInfoInteger(((BukkitPlayer)player.getBase()).getPlayer(), "homes", 0);
	}

	@Override
	public MessageFormat getChatFormat(final IUser player)
	{
		String format = getRawChatFormat(player);
		format = Util.replaceFormat(format);
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
		String chatformat = chat.getPlayerInfoString(((BukkitPlayer)player.getBase()).getPlayer(), "chatformat", "");
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
		return chat.getPrimaryGroup(((BukkitPlayer)player.getBase()).getPlayer());
	}

	@Override
	public boolean inGroup(IUser player, String groupname)
	{
		Chat chat = ess.getServer().getServiceProvider(Chat.class);
		for (String group : chat.getPlayerGroups(((BukkitPlayer)player.getBase()).getPlayer()))
		{
			if (group.equalsIgnoreCase(groupname))
			{
				return true;
			}
		}
		return false;
	}
}
