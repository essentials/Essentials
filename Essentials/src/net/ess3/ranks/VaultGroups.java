package net.ess3.ranks;

import java.util.logging.Level;
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

	private <T> T getServiceProvider(final Class<T> clazz)
	{
		final RegisteredServiceProvider<T> provider = ess.getServer().getServicesManager().getRegistration(clazz);
		return provider.getProvider();
	}

	private double getVaultDouble(final CommandSender player, final String key, final double defaultValue)
	{
		try
		{
			final Chat chat = getServiceProvider(Chat.class);
			return chat.getPlayerInfoDouble(getPlayer(player), key, defaultValue);
		}
		catch (Exception e)
		{
			ess.getLogger().log(Level.WARNING, e.getMessage(), e);
			return defaultValue;
		}
	}

	private int getVaultInteger(final CommandSender player, final String key, final int defaultValue)
	{
		try
		{
			final Chat chat = getServiceProvider(Chat.class);
			return chat.getPlayerInfoInteger(getPlayer(player), key, defaultValue);
		}
		catch (Exception e)
		{
			ess.getLogger().log(Level.WARNING, e.getMessage(), e);
			return defaultValue;
		}
	}

	private String getVaultString(final CommandSender player, final String key, final String defaultValue)
	{
		try
		{
			final Chat chat = getServiceProvider(Chat.class);
			return chat.getPlayerInfoString(getPlayer(player), key, defaultValue);
		}
		catch (Exception e)
		{
			ess.getLogger().log(Level.WARNING, e.getMessage(), e);
			return defaultValue;
		}
	}

	@Override
	public double getHealCooldown(final CommandSender player)
	{
		return getVaultDouble(player, "healcooldown", 0);
	}

	@Override
	public double getTeleportCooldown(final CommandSender player)
	{
		return getVaultDouble(player, "teleportcooldown", 0);
	}

	@Override
	public double getTeleportDelay(final CommandSender player)
	{
		return getVaultDouble(player, "teleportdelay", 0);
	}

	@Override
	public String getPrefix(final CommandSender player)
	{
		try
		{
			final Chat chat = getServiceProvider(Chat.class);
			return chat.getPlayerPrefix(getPlayer(player));
		}
		catch (Exception e)
		{
			ess.getLogger().log(Level.WARNING, e.getMessage(), e);
			return null;
		}
	}

	@Override
	public String getSuffix(final CommandSender player)
	{
		try
		{
			final Chat chat = getServiceProvider(Chat.class);
			return chat.getPlayerSuffix(getPlayer(player));
		}
		catch (Exception e)
		{
			ess.getLogger().log(Level.WARNING, e.getMessage(), e);
			return null;
		}
	}

	@Override
	public int getHomeLimit(final CommandSender player)
	{
		return getVaultInteger(player, "homes", 1);
	}

	@Override
	protected String getRawChatFormat(final CommandSender player)
	{
		final String chatformat = getVaultString(player, "chatformat", null);
		if (chatformat != null && !chatformat.isEmpty())
		{
			return chatformat;
		}

		final ISettings settings = ess.getSettings();
		return settings.getData().getChat().getDefaultFormat();
	}

	@Override
	public String getMainGroup(final CommandSender player)
	{
		try
		{
			final Chat chat = getServiceProvider(Chat.class);
			return chat.getPrimaryGroup(getPlayer(player));
		}
		catch (Exception e)
		{
			ess.getLogger().log(Level.WARNING, e.getMessage(), e);
			return null;
		}
	}

	@Override
	public boolean inGroup(final CommandSender player, final String groupname)
	{
		try
		{
			final Chat chat = getServiceProvider(Chat.class);
			for (String group : chat.getPlayerGroups(getPlayer(player)))
			{
				if (group.equalsIgnoreCase(groupname))
				{
					return true;
				}
			}
		}
		catch (Exception e)
		{
			ess.getLogger().log(Level.WARNING, e.getMessage(), e);
		}
		return false;
	}
}
