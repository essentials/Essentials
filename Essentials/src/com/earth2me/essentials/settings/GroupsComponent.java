package com.earth2me.essentials.settings;

import com.earth2me.essentials.Util;
import com.earth2me.essentials.api.IContext;
import com.earth2me.essentials.api.IGroupsComponent;
import com.earth2me.essentials.api.ISettingsComponent;
import com.earth2me.essentials.components.users.IUser;
import com.earth2me.essentials.perm.GroupsPermissions;
import com.earth2me.essentials.storage.AsyncStorageObjectHolder;
import java.io.File;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import lombok.Cleanup;


public class GroupsComponent extends AsyncStorageObjectHolder<Groups> implements IGroupsComponent
{
	public GroupsComponent(final IContext context)
	{
		super(context, Groups.class);
	}

	@Override
	public String getTypeId()
	{
		return "GroupsComponent";
	}

	@Override
	public void initialize()
	{
	}

	@Override
	public void onEnable()
	{
		reload();
	}

	@Override
	public File getStorageFile()
	{
		return new File(context.getDataFolder(), "groups.yml");
	}

	public Collection<Entry<String, GroupOptions>> getGroups(final IUser player)
	{
		acquireReadLock();
		try
		{
			final Map<String, GroupOptions> groups = getData().getGroups();
			if (groups == null || groups.isEmpty())
			{
				return Collections.emptyList();
			}
			final ArrayList<Entry<String, GroupOptions>> list = new ArrayList<Entry<String, GroupOptions>>();
			for (Entry<String, GroupOptions> entry : groups.entrySet())
			{
				if (GroupsPermissions.getPermission(entry.getKey()).isAuthorized(player))
				{
					if(entry.getValue() != null)
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
		for (Entry<String, GroupOptions> groupOptions : getGroups(player))
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
		for (Entry<String, GroupOptions> groupOptions : getGroups(player))
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
		for (Entry<String, GroupOptions> groupOptions : getGroups(player))
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
		for (Entry<String, GroupOptions> groupOptions : getGroups(player))
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
		for (Entry<String, GroupOptions> groupOptions : getGroups(player))
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
		for (Entry<String, GroupOptions> groupOptions : getGroups(player))
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
		for (Entry<String, GroupOptions> groupOptions : getGroups(player))
		{
			if (groupOptions.getValue().getMessageFormat() != null)
			{
				return groupOptions.getValue().getMessageFormat();
			}
		}
		@Cleanup
		ISettingsComponent settings = context.getSettings();
		settings.acquireReadLock();
		return settings.getData().getChat().getDefaultFormat();
	}

	@Override
	public boolean isInGroup(IUser player, String groupname)
	{
		for (Entry<String, GroupOptions> groupOptions : getGroups(player))
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
		for (Entry<String, GroupOptions> groupOptions : getGroups(player))
		{
			return groupOptions.getKey();
		}
		return "default";
	}

}
