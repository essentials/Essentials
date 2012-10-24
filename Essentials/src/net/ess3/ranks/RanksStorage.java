package net.ess3.ranks;

import java.io.File;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import net.ess3.api.IEssentials;
import net.ess3.api.IRanks;
import net.ess3.api.ISettings;
import net.ess3.permissions.Permissions;
import net.ess3.storage.AsyncStorageObjectHolder;
import org.bukkit.command.CommandSender;


public class RanksStorage extends AsyncStorageObjectHolder<Ranks> implements IRanks
{
	private AbstractRanks abstractRanks = new AbstractRanks()
	{
		@Override
		protected String getRawChatFormat(final CommandSender sender)
		{
			return RanksStorage.this.getRawChatFormat(sender);
		}
	};

	public RanksStorage(final IEssentials ess)
	{
		super(ess, Ranks.class, new File(ess.getPlugin().getDataFolder(), "ranks.yml"));
		onReload();
	}

	public Collection<Entry<String, RankOptions>> getGroups(final CommandSender player)
	{
		final Map<String, RankOptions> groups = getData().getRanks();
		if (groups == null || groups.isEmpty())
		{
			return Collections.emptyList();
		}
		final ArrayList<Entry<String, RankOptions>> list = new ArrayList();
		for (Entry<String, RankOptions> entry : groups.entrySet())
		{
			if (Permissions.RANKS.isAuthorized(player, entry.getKey()))
			{
				if (entry.getValue() != null)
				{
					list.add(entry);
				}
			}
		}
		return list;
	}

	@Override
	public double getHealCooldown(final CommandSender player)
	{
		for (Entry<String, RankOptions> groupOptions : getGroups(player))
		{
			if (groupOptions.getValue().getHealCooldown() != null)
			{
				return groupOptions.getValue().getHealCooldown();
			}
		}
		return 0;
	}

	@Override
	public double getTeleportCooldown(final CommandSender player)
	{
		for (Entry<String, RankOptions> groupOptions : getGroups(player))
		{
			if (groupOptions.getValue().getTeleportCooldown() != null)
			{
				return groupOptions.getValue().getTeleportCooldown();
			}
		}
		return 0;
	}

	@Override
	public double getTeleportDelay(final CommandSender player)
	{
		for (Entry<String, RankOptions> groupOptions : getGroups(player))
		{
			if (groupOptions.getValue().getTeleportDelay() != null)
			{
				return groupOptions.getValue().getTeleportDelay();
			}
		}
		return 0;
	}

	@Override
	public String getPrefix(final CommandSender player)
	{
		for (Entry<String, RankOptions> groupOptions : getGroups(player))
		{
			if (groupOptions.getValue().getPrefix() != null)
			{
				return groupOptions.getValue().getPrefix();
			}
		}
		return "";
	}

	@Override
	public String getSuffix(final CommandSender player)
	{
		for (Entry<String, RankOptions> groupOptions : getGroups(player))
		{
			if (groupOptions.getValue().getSuffix() != null)
			{
				return groupOptions.getValue().getSuffix();
			}
		}
		return "";
	}

	@Override
	public int getHomeLimit(final CommandSender player)
	{
		for (Entry<String, RankOptions> groupOptions : getGroups(player))
		{
			if (groupOptions.getValue().getHomes() != null)
			{
				return groupOptions.getValue().getHomes();
			}
		}
		return 1;
	}

	@Override
	public MessageFormat getChatFormat(final CommandSender player)
	{
		return abstractRanks.getChatFormat(player);
	}

	private String getRawChatFormat(final CommandSender player)
	{
		for (Entry<String, RankOptions> groupOptions : getGroups(player))
		{
			if (groupOptions.getValue().getChatFormat() != null)
			{
				return groupOptions.getValue().getChatFormat();
			}
		}

		ISettings settings = ess.getSettings();
		return settings.getData().getChat().getDefaultFormat();
	}

	@Override
	public boolean inGroup(final CommandSender player, final String groupname)
	{
		for (Entry<String, RankOptions> groupOptions : getGroups(player))
		{
			if (groupOptions.getKey().equalsIgnoreCase(groupname))
			{
				return true;
			}
		}
		return false;
	}

	@Override
	public String getMainGroup(final CommandSender player)
	{
		for (Entry<String, RankOptions> groupOptions : getGroups(player))
		{
			return groupOptions.getKey();
		}
		return "default";
	}
}
