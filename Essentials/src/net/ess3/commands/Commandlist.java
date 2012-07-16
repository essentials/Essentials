package net.ess3.commands;

import static net.ess3.I18n._;
import net.ess3.utils.Util;
import net.ess3.api.ISettings;
import net.ess3.api.IUser;
import net.ess3.permissions.Permissions;
import java.util.*;


public class Commandlist extends EssentialsCommand
{
	@Override
	protected void run(final CommandSender sender, final String commandLabel, final String[] args) throws Exception
	{
		boolean showhidden = false;
		if (Permissions.LIST_HIDDEN.isAuthorized(sender))
		{
			showhidden = true;
		}
		int playerHidden = 0;
		for (Player onlinePlayer : server.getOnlinePlayers())
		{
			if (onlinePlayer.getUser().isHidden())
			{
				playerHidden++;
			}
		}

		String online;
		if (showhidden && playerHidden > 0)
		{
			online = _("listAmountHidden", server.getOnlinePlayers().size() - playerHidden, playerHidden, server.getMaxPlayers());
		}
		else
		{
			online = _("listAmount", server.getOnlinePlayers().size() - playerHidden, server.getMaxPlayers());
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
			for (Player onlinePlayer : server.getOnlinePlayers())
			{
				final IUser player = onlinePlayer.getUser();
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
				groupString.append(_("listGroupTag",Util.replaceColor(group)));
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
			for (Player onlinePlayer : server.getOnlinePlayers())
			{
				final IUser player =onlinePlayer.getUser();
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
