package org.mcess.essentials.commands;

import org.mcess.essentials.CommandSource;
import org.mcess.essentials.User;
import org.mcess.essentials.UserMap;
import org.mcess.essentials.craftbukkit.BanLookup;
import org.mcess.essentials.utils.DateUtil;
import org.mcess.essentials.utils.FormatUtil;
import org.mcess.essentials.utils.StringUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.bukkit.BanList;
import java.util.UUID;
import org.bukkit.BanEntry;
import org.bukkit.Location;
import org.bukkit.Server;
import org.mcess.essentials.I18n;


public class Commandseen extends EssentialsCommand
{
	public Commandseen()
	{
		super("seen");
	}

	@Override
	protected void run(final Server server, final CommandSource sender, final String commandLabel, final String[] args) throws Exception
	{
		seen(server, sender, args, true, true, true);
	}

	@Override
	protected void run(final Server server, final User user, final String commandLabel, final String[] args) throws Exception
	{
		seen(server, user.getSource(), args, user.isAuthorized("essentials.seen.banreason"), user.isAuthorized("essentials.seen.extra"), user.isAuthorized("essentials.seen.ipsearch"));
	}

	protected void seen(final Server server, final CommandSource sender, final String[] args, final boolean showBan, final boolean extra, final boolean ipLookup) throws Exception
	{
		if (args.length < 1)
		{
			throw new NotEnoughArgumentsException();
		}
		User player = ess.getOfflineUser(args[0]);
		if (player == null)
		{
			if (ipLookup && FormatUtil.validIP(args[0]))
			{
				seenIP(server, sender, args[0]);
				return;
			}
			else if (ess.getServer().getBanList(BanList.Type.IP).isBanned(args[0]))
			{
				sender.sendMessage(I18n.tl("isIpBanned", args[0]));
				return;
			}
			else if (BanLookup.isBanned(ess, args[0]))
			{
				sender.sendMessage(I18n.tl("whoisBanned", showBan ? BanLookup.getBanEntry(ess, args[0]).getReason() : I18n.tl("true")));
				return;
			}
			else
			{
				try
				{
					player = getPlayer(server, sender, args, 0);
				}
				catch (NoSuchFieldException e)
				{
					throw new PlayerNotFoundException();
				}
			}
		}
		if (player.getBase().isOnline() && canInteractWith(sender, player))
		{
			seenOnline(server, sender, player, showBan, extra);
		}
		else
		{
			seenOffline(server, sender, player, showBan, extra);
		}

	}

	private void seenOnline(final Server server, final CommandSource sender, final User user, final boolean showBan, final boolean extra) throws Exception
	{

		user.setDisplayNick();
		sender.sendMessage(I18n.tl("seenOnline", user.getDisplayName(), DateUtil.formatDateDiff(user.getLastLogin())));

		if (ess.getSettings().isDebug())
		{
			ess.getLogger().info("UUID: " + user.getBase().getUniqueId().toString());
		}

		List<String> history = ess.getUserMap().getUserHistory(user.getBase().getUniqueId());
		if (history != null && history.size() > 1)
		{
			sender.sendMessage(I18n.tl("seenAccounts", StringUtil.joinListSkip(", ", user.getName(), history)));
		}

		if (user.isAfk())
		{
			sender.sendMessage(I18n.tl("whoisAFK", I18n.tl("true")));
		}
		if (user.isJailed())
		{
			sender.sendMessage(I18n.tl("whoisJail", (user.getJailTimeout() > 0
					? DateUtil.formatDateDiff(user.getJailTimeout())
					: I18n.tl("true"))));
		}
		if (user.isMuted())
		{
			sender.sendMessage(I18n.tl("whoisMuted", (user.getMuteTimeout() > 0
					? DateUtil.formatDateDiff(user.getMuteTimeout())
					: I18n.tl("true"))));
		}
		final String location = user.getGeoLocation();
		if (location != null && (!(sender.isPlayer()) || ess.getUser(sender.getPlayer()).isAuthorized("essentials.geoip.show")))
		{
			sender.sendMessage(I18n.tl("whoisGeoLocation", location));
		}
		if (extra)
		{
			sender.sendMessage(I18n.tl("whoisIPAddress", user.getBase().getAddress().getAddress().toString()));
		}
	}

	private void seenOffline(final Server server, final CommandSource sender, User user, final boolean showBan, final boolean extra) throws Exception
	{
		user.setDisplayNick();
		if (user.getLastLogout() > 0)
		{
			sender.sendMessage(I18n.tl("seenOffline", user.getName(), DateUtil.formatDateDiff(user.getLastLogout())));
		}
		else
		{
			sender.sendMessage(I18n.tl("userUnknown", user.getName()));
		}

		if (ess.getSettings().isDebug())
		{
			ess.getLogger().info("UUID: " + user.getBase().getUniqueId().toString());
		}

		List<String> history = ess.getUserMap().getUserHistory(user.getBase().getUniqueId());
		if (history != null && history.size() > 1)
		{
			sender.sendMessage(I18n.tl("seenAccounts", StringUtil.joinListSkip(", ", user.getName(), history)));
		}

		if (BanLookup.isBanned(ess, user))
		{
			final BanEntry banEntry = BanLookup.getBanEntry(ess, user.getName());
			final String reason = showBan ? banEntry.getReason() : I18n.tl("true");
			sender.sendMessage(I18n.tl("whoisBanned", reason));
			if (banEntry.getExpiration() != null)
			{
				Date expiry = banEntry.getExpiration();
				String expireString = I18n.tl("now");
				if (expiry.after(new Date()))
				{
					expireString = DateUtil.formatDateDiff(expiry.getTime());
				}
				sender.sendMessage(I18n.tl("whoisTempBanned", expireString));
			}
		}

		final String location = user.getGeoLocation();
		if (location != null && (!(sender.isPlayer()) || ess.getUser(sender.getPlayer()).isAuthorized("essentials.geoip.show")))
		{
			sender.sendMessage(I18n.tl("whoisGeoLocation", location));
		}
		if (extra)
		{
			if (!user.getLastLoginAddress().isEmpty())
			{
				sender.sendMessage(I18n.tl("whoisIPAddress", user.getLastLoginAddress()));
			}
			final Location loc = user.getLogoutLocation();
			if (loc != null)
			{
				sender.sendMessage(I18n.tl("whoisLocation", loc.getWorld().getName(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()));
			}
		}
	}

	private void seenIP(final Server server, final CommandSource sender, final String ipAddress) throws Exception
	{
		final UserMap userMap = ess.getUserMap();

		if (ess.getServer().getBanList(BanList.Type.IP).isBanned(ipAddress))
		{
			sender.sendMessage(I18n.tl("isIpBanned", ipAddress));
		}

		sender.sendMessage(I18n.tl("runningPlayerMatch", ipAddress));

		ess.runTaskAsynchronously(new Runnable()
		{
			@Override
			public void run()
			{
				final List<String> matches = new ArrayList<String>();
				for (final UUID u : userMap.getAllUniqueUsers())
				{
					final User user = ess.getUserMap().getUser(u);
					if (user == null)
					{
						continue;
					}

					final String uIPAddress = user.getLastLoginAddress();

					if (!uIPAddress.isEmpty() && uIPAddress.equalsIgnoreCase(ipAddress))
					{
						matches.add(user.getName());
					}
				}

				if (matches.size() > 0)
				{
					sender.sendMessage(I18n.tl("matchingIPAddress"));
					sender.sendMessage(StringUtil.joinList(matches));
				}
				else
				{
					sender.sendMessage(I18n.tl("noMatchingPlayers"));
				}

			}
		});

	}
}
