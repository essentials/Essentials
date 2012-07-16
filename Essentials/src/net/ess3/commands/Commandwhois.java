package net.ess3.commands;

import static net.ess3.I18n._;
import net.ess3.utils.Util;
import net.ess3.api.ISettings;
import net.ess3.api.IUser;
import net.ess3.api.server.CommandSender;
import net.ess3.api.server.Player;
import net.ess3.permissions.Permissions;
import net.ess3.user.UserData;
import net.ess3.utils.DateUtil;
import java.util.Locale;
import lombok.Cleanup;



public class Commandwhois extends EssentialsCommand
{
	@Override
	protected void run(final CommandSender sender, final String commandLabel, final String[] args) throws Exception
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
		for (Player onlinePlayer : server.getOnlinePlayers())
		{
			@Cleanup
			final IUser user = onlinePlayer.getUser();

			if (user.isHidden() && !showhidden)
			{
				continue;
			}
			user.acquireReadLock();
			final String nickName = Util.stripColor(user.getData().getNickname());
			if (!whois.equalsIgnoreCase(nickName)
				&& !whois.substring(prefixLength).equalsIgnoreCase(nickName)
				&& !whois.equalsIgnoreCase(user.getName()))
			{
				continue;
			}
			sender.sendMessage("");
			sender.sendMessage(_("whoisIs", user.getDisplayName(), user.getName()));
			sender.sendMessage(_("whoisHealth", user.getHealth()));
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
		}
	}
}
