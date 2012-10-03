package net.ess3.ranks;

import java.io.File;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import lombok.Cleanup;
import net.ess3.api.IEssentials;
import net.ess3.api.IRanks;
import net.ess3.api.ISettings;
import net.ess3.api.IUser;
import net.ess3.permissions.Permissions;
import net.ess3.storage.AsyncStorageObjectHolder;
import net.ess3.utils.FormatUtil;


public class RanksStorage extends AsyncStorageObjectHolder<Ranks> implements IRanks
{
	@Override
	public void finishRead()
	{
	}

	@Override
	public void finishWrite()
	{
	}

	public RanksStorage(final IEssentials ess)
	{
		super(ess, Ranks.class);
		onReload();
	}

	@Override
	public File getStorageFile()
	{
		return new File(ess.getPlugin().getDataFolder(), "ranks.yml");
	}

	public Collection<Entry<String, RankOptions>> getGroups(final IUser player)
	{
		acquireReadLock();
		try
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
		finally
		{
			unlock();
		}
	}

	@Override
	public double getHealCooldown(final IUser player)
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
	public double getTeleportCooldown(final IUser player)
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
	public double getTeleportDelay(final IUser player)
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
	public String getPrefix(final IUser player)
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
	public String getSuffix(final IUser player)
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
	public int getHomeLimit(final IUser player)
	{
		for (Entry<String, RankOptions> groupOptions : getGroups(player))
		{
			if (groupOptions.getValue().getHomes() != null)
			{
				return groupOptions.getValue().getHomes();
			}
		}
		return 0;
	}

	//TODO: Reimplement caching
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
		for (Entry<String, RankOptions> groupOptions : getGroups(player))
		{
			if (groupOptions.getValue().getMessageFormat() != null)
			{
				return groupOptions.getValue().getMessageFormat();
			}
		}
		@Cleanup
		ISettings settings = ess.getSettings();
		settings.acquireReadLock();
		return settings.getData().getChat().getDefaultFormat();
	}

	@Override
	public boolean inGroup(IUser player, String groupname)
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
	public String getMainGroup(IUser player)
	{
		for (Entry<String, RankOptions> groupOptions : getGroups(player))
		{
			return groupOptions.getKey();
		}
		return "default";
	}
}
