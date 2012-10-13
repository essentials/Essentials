package net.ess3.ranks;

import net.ess3.api.IEssentials;
import net.ess3.api.IRanks;
import net.ess3.api.ISettings;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.RegisteredServiceProvider;


public class VaultGroups extends AbstractRanks implements IRanks
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
	public double getHealCooldown(CommandSender player)
	{
		Chat chat = getServiceProvider(Chat.class);
		return chat.getPlayerInfoDouble(getPlayer(player), "healcooldown", 0);
	}

	@Override
	public double getTeleportCooldown(CommandSender player)
	{
		Chat chat = getServiceProvider(Chat.class);
		return chat.getPlayerInfoDouble(getPlayer(player), "teleportcooldown", 0);
	}

	@Override
	public double getTeleportDelay(CommandSender player)
	{
		Chat chat = getServiceProvider(Chat.class);
		return chat.getPlayerInfoDouble(getPlayer(player), "teleportdelay", 0);
	}

	@Override
	public String getPrefix(CommandSender player)
	{
		Chat chat = getServiceProvider(Chat.class);
		return chat.getPlayerPrefix(getPlayer(player));
	}

	@Override
	public String getSuffix(CommandSender player)
	{
		Chat chat = getServiceProvider(Chat.class);
		return chat.getPlayerSuffix(getPlayer(player));
	}

	@Override
	public int getHomeLimit(CommandSender player)
	{
		Chat chat = getServiceProvider(Chat.class);
		return chat.getPlayerInfoInteger(getPlayer(player), "homes", 0);
	}

	@Override
	protected String getRawChatFormat(final CommandSender player)
	{
		Chat chat = getServiceProvider(Chat.class);
		String chatformat = chat.getPlayerInfoString(getPlayer(player), "chatformat", "");
		if (chatformat != null && !chatformat.isEmpty())
		{
			return chatformat;
		}

		ISettings settings = ess.getSettings();
		return settings.getData().getChat().getDefaultFormat();
	}

	@Override
	public String getMainGroup(CommandSender player)
	{
		Chat chat = getServiceProvider(Chat.class);
		return chat.getPrimaryGroup(getPlayer(player));
	}

	@Override
	public boolean inGroup(CommandSender player, String groupname)
	{
		Chat chat = getServiceProvider(Chat.class);
		for (String group : chat.getPlayerGroups(getPlayer(player)))
		{
			if (group.equalsIgnoreCase(groupname))
			{
				return true;
			}
		}
		return false;
	}
}
