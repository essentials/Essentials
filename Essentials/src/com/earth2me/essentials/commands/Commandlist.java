package com.earth2me.essentials.commands;

import static com.earth2me.essentials.I18n._;
import com.earth2me.essentials.api.ISettings;
import com.earth2me.essentials.api.IUser;
import com.earth2me.essentials.permissions.Permissions;
import com.earth2me.essentials.utils.Util;
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
		for (Player onlinePlayer : server.getOnlinePlayers())
		{
			if (ess.getUser(onlinePlayer).isHidden())
			{
				playerHidden++;
			}
		}

		String online;
		if (showhidden && playerHidden > 0)
		{
			online = _("listAmountHidden", server.getOnlinePlayers().length - playerHidden, playerHidden, server.getMaxPlayers());
		}
		else
		{
			online = _("listAmount", server.getOnlinePlayers().length - playerHidden, server.getMaxPlayers());
		}
		sender.sendMessage(online);

		boolean sortListByGroups = false;
		ISettings settings = ess.getSettings();
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
			for (Player OnlinePlayer : server.getOnlinePlayers())
			{
				final IUser player = ess.getUser(OnlinePlayer);
				if (player.isHidden() && !showhidden)
				{
					continue;
				}
				final String group = ess.getRanks().getMainGroup(player);
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
				groupString.append(_("listGroupTag",Util.replaceFormat(group)));
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
					user.setDisplayNick();
					groupString.append(user.getDisplayName());
					groupString.append("§f");
				}
				sender.sendMessage(groupString.toString());
			}
		}
		else
		{
			final List<IUser> users = new ArrayList<IUser>();
			for (Player OnlinePlayer : server.getOnlinePlayers())
			{
				final IUser player = ess.getUser(OnlinePlayer);
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
				user.setDisplayNick();
				onlineUsers.append(user.getDisplayName());
				onlineUsers.append("§f");
			}
			sender.sendMessage(onlineUsers.toString());
		}
	}
}
