package com.earth2me.essentials.commands;

import static com.earth2me.essentials.I18n._;
import com.earth2me.essentials.utils.Util;
import com.earth2me.essentials.api.ISettings;
import com.earth2me.essentials.api.IUser;
import com.earth2me.essentials.permissions.Permissions;
import com.earth2me.essentials.user.UserData;
import com.earth2me.essentials.utils.DateUtil;
import com.earth2me.essentials.craftbukkit.SetExpFix;
import java.util.Locale;
import lombok.Cleanup;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class Commandwhois extends EssentialsCommand
{
	@Override
	public void run(final CommandSender sender, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 1)
		{
			throw new NotEnoughArgumentsException();
		}
		boolean showhidden = false;
		if (sender instanceof Player)
		{
			if (Permissions.LIST_HIDDEN.isAuthorized(sender))
			{
				showhidden = true;
			}
		}
		else
		{
			showhidden = true;
		}
		final String whois = args[0].toLowerCase(Locale.ENGLISH);
		@Cleanup
		ISettings settings = ess.getSettings();
		settings.acquireReadLock();
		final int prefixLength = Util.stripColor(settings.getData().getChat().getNicknamePrefix()).length();
		boolean foundPlayer = false;
		for (Player onlinePlayer : server.getOnlinePlayers())
		{
			@Cleanup
			final IUser user = ess.getUser(onlinePlayer);

			if (user.isHidden() && !showhidden)
			{
				continue;
			}
			user.acquireReadLock();
			final String nickName = Util.stripFormat(user.getData().getNickname());
			if (!whois.equalsIgnoreCase(nickName)
				&& !whois.substring(prefixLength).equalsIgnoreCase(nickName)
				&& !whois.equalsIgnoreCase(user.getName()))
			{
				continue;
			}
			foundPlayer = true;
			sender.sendMessage("");
			user.setDisplayNick();
			sender.sendMessage(_("whoisIs", user.getDisplayName(), user.getName()));
			sender.sendMessage(_("whoisHealth", user.getHealth()));
			sender.sendMessage(_("whoisExp", SetExpFix.getTotalExperience(user), user.getLevel()));
			sender.sendMessage(_("whoisOP", (user.isOp() ? _("true") : _("false"))));
			sender.sendMessage(_("whoisGod", (user.isGodModeEnabled() ? _("true") : _("false"))));
			sender.sendMessage(_("whoisGamemode", _(user.getGameMode().toString().toLowerCase(Locale.ENGLISH))));
			sender.sendMessage(_("whoisLocation", user.getLocation().getWorld().getName(), user.getLocation().getBlockX(), user.getLocation().getBlockY(), user.getLocation().getBlockZ()));
			sender.sendMessage(_("whoisMoney", Util.displayCurrency(user.getMoney(), ess)));
			sender.sendMessage(_("whoisJail", (user.getData().isJailed()
											   ? user.getTimestamp(UserData.TimestampType.JAIL) > 0
												 ? DateUtil.formatDateDiff(user.getTimestamp(UserData.TimestampType.JAIL))
												 : _("true")
											   : _("false"))));
			sender.sendMessage(user.getData().isAfk()
							   ? _("whoisStatusAway")
							   : _("whoisStatusAvailable"));
			sender.sendMessage(_("whoisIPAddress", user.getAddress().getAddress().toString()));
			final String location = user.getData().getGeolocation();
			if (location != null
				&& Permissions.GEOIP_SHOW.isAuthorized(sender))
			{
				sender.sendMessage(_("whoisGeoLocation", location));
			}
			if (!foundPlayer)
			{
				throw new NoSuchFieldException(_("playerNotFound"));
			}
		}
	}
}
