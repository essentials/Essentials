package com.earth2me.essentials.components.commands.handlers;

import static com.earth2me.essentials.I18nComponent._;
import com.earth2me.essentials.api.ISettingsComponent;
import com.earth2me.essentials.components.commands.EssentialsCommand;
import com.earth2me.essentials.components.users.IUser;
import com.earth2me.essentials.perm.Permissions;
import java.util.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class Commandlist extends EssentialsCommand
{
	@Override
	public void run(final CommandSender sender, final String commandLabel, final String[] args) throws Exception
	{
		boolean showhidden = false;
		if (Permissions.LIST_HIDDEN.isAuthorized(sender))
		{
			showhidden = true;
		}
		int playerHidden = 0;
		for (Player onlinePlayer : getServer().getOnlinePlayers())
		{
			if (getContext().getUser(onlinePlayer).isHidden())
			{
				playerHidden++;
			}
		}

		String online;
		if (showhidden && playerHidden > 0)
		{
			online = _("listAmountHidden", getServer().getOnlinePlayers().length - playerHidden, playerHidden, getServer().getMaxPlayers());
		}
		else
		{
			online = _("listAmount", getServer().getOnlinePlayers().length - playerHidden, getServer().getMaxPlayers());
		}
		sender.sendMessage(online);

		boolean sortListByGroups = false;
		ISettingsComponent settings = getContext().getSettings();
		settings.acquireReadLock();
		try
		{
			sortListByGroups = settings.getData().getCommands().getList().isSortByGroups();
		}
		finally
		{
			settings.unlock();
		}

		if (sortListByGroups)
		{
			Map<String, List<IUser>> sort = new HashMap<String, List<IUser>>();
			for (Player OnlinePlayer : getServer().getOnlinePlayers())
			{
				final IUser player = getContext().getUser(OnlinePlayer);
				if (player.isHidden() && !showhidden)
				{
					continue;
				}
				final String group = getContext().getGroups().getMainGroup(player);
				List<IUser> list = sort.get(group);
				if (list == null)
				{
					list = new ArrayList<IUser>();
					sort.put(group, list);
				}
				list.add(player);
			}
			final String[] groups = sort.keySet().toArray(new String[0]);
			Arrays.sort(groups, String.CASE_INSENSITIVE_ORDER);
			for (String group : groups)
			{
				final StringBuilder groupString = new StringBuilder();
				groupString.append(group).append(": ");
				final List<IUser> users = sort.get(group);
				Collections.sort(users);
				boolean first = true;
				for (IUser user : users)
				{
					if (!first)
					{
						groupString.append(", ");
					}
					else
					{
						first = false;
					}
					user.acquireReadLock();
					try
					{
						if (user.getData().isAfk())
						{
							groupString.append(_("listAfkTag"));
						}
					}
					finally
					{
						user.unlock();
					}
					if (user.isHidden())
					{
						groupString.append(_("listHiddenTag"));
					}
					groupString.append(user.getDisplayName());
					groupString.append("§f");
				}
				sender.sendMessage(groupString.toString());
			}
		}
		else
		{
			final List<IUser> users = new ArrayList<IUser>();
			for (Player OnlinePlayer : getServer().getOnlinePlayers())
			{
				final IUser player = getContext().getUser(OnlinePlayer);
				if (player.isHidden() && !showhidden)
				{
					continue;
				}
				users.add(player);
			}
			Collections.sort(users);

			final StringBuilder onlineUsers = new StringBuilder();
			onlineUsers.append(_("connectedPlayers"));
			boolean first = true;
			for (IUser user : users)
			{
				if (!first)
				{
					onlineUsers.append(", ");
				}
				else
				{
					first = false;
				}
				user.acquireReadLock();
				try
				{
					if (user.getData().isAfk())
					{
						onlineUsers.append(_("listAfkTag"));
					}
				}
				finally
				{
					user.unlock();
				}
				if (user.isHidden())
				{
					onlineUsers.append(_("listHiddenTag"));
				}
				onlineUsers.append(user.getDisplayName());
				onlineUsers.append("§f");
			}
			sender.sendMessage(onlineUsers.toString());
		}
	}
}
