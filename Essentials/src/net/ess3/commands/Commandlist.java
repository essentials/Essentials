package net.ess3.commands;

import java.util.*;
import static net.ess3.I18n._;
import net.ess3.api.ISettings;
import net.ess3.api.IUser;
import net.ess3.permissions.Permissions;
import net.ess3.utils.FormatUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class Commandlist extends EssentialsCommand
{
	@Override
	protected void run(final CommandSender sender, final String commandLabel, final String[] args) throws Exception
	{
		String online;
		boolean showhidden = false;
		if (Permissions.LIST_HIDDEN.isAuthorized(sender))
		{
			showhidden = true;
		}
		Player userPlayer = getPlayerOrNull(sender);
		if (userPlayer != null)
		{
			int playerHidden = 0;
			for (Player onlinePlayer : server.getOnlinePlayers())
			{
				if (!userPlayer.canSee(onlinePlayer))
				{
					playerHidden++;
				}
			}

			if (showhidden && playerHidden > 0)
			{
				online = _("listAmountHidden", server.getOnlinePlayers().length - playerHidden, playerHidden, server.getMaxPlayers());
			}
			else
			{
				online = _("listAmount", server.getOnlinePlayers().length - playerHidden, server.getMaxPlayers());
			}
		}
		else
		{
			online = _("listAmount", server.getOnlinePlayers().length, server.getMaxPlayers());
		}

		sender.sendMessage(online);

		ISettings settings = ess.getSettings();

		boolean sortListByGroups = sortListByGroups = settings.getData().getCommands().getList().isSortByGroups();

		if (sortListByGroups)
		{
			Map<String, List<IUser>> sort = new HashMap<String, List<IUser>>();
			Set<String> hiddenPlayers = new HashSet<String>();
			for (Player onlinePlayer : server.getOnlinePlayers())
			{
				if (userPlayer != null
					&& !userPlayer.canSee(onlinePlayer))
				{
					hiddenPlayers.add(onlinePlayer.getName());
					if (!showhidden)
					{
						continue;
					}
				}
				final String group = ess.getRanks().getMainGroup(onlinePlayer);
				List<IUser> list = sort.get(group);
				if (list == null)
				{
					list = new ArrayList<IUser>();
					sort.put(group, list);
				}
				list.add(getUser(onlinePlayer));
			}
			final String[] groups = sort.keySet().toArray(new String[0]);
			Arrays.sort(groups, String.CASE_INSENSITIVE_ORDER);
			for (String group : groups)
			{
				final StringBuilder groupString = new StringBuilder();
				groupString.append(_("listGroupTag", FormatUtil.replaceFormat(group)));
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

					if (user.getData().isAfk())
					{
						groupString.append(_("listAfkTag"));
					}

					if (hiddenPlayers.contains(user.getName()))
					{
						groupString.append(_("listHiddenTag"));
					}
					groupString.append(user.getPlayer().getDisplayName());
					groupString.append("§f");
				}
				sender.sendMessage(groupString.toString());
			}
		}
		else
		{
			final List<IUser> users = new ArrayList<IUser>();
			Set<String> hiddenPlayers = new HashSet<String>();
			for (Player onlinePlayer : server.getOnlinePlayers())
			{
				if (userPlayer != null
					&& !userPlayer.canSee(onlinePlayer))
				{
					hiddenPlayers.add(onlinePlayer.getName());
					if (!showhidden)
					{
						continue;
					}
				}
				users.add(getUser(onlinePlayer));
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

				if (user.getData().isAfk())
				{
					onlineUsers.append(_("listAfkTag"));
				}

				if (hiddenPlayers.contains(user.getName()))
				{
					onlineUsers.append(_("listHiddenTag"));
				}
				onlineUsers.append(user.getPlayer().getDisplayName());
				onlineUsers.append("§f");
			}
			sender.sendMessage(onlineUsers.toString());
		}
	}
}
