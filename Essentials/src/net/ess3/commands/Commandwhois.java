package net.ess3.commands;

import java.util.Locale;
import static net.ess3.I18n._;
import net.ess3.api.ISettings;
import net.ess3.api.IUser;
import net.ess3.craftbukkit.SetExpFix;
import net.ess3.permissions.Permissions;
import net.ess3.user.UserData;
import net.ess3.utils.DateUtil;
import net.ess3.utils.FormatUtil;
import net.ess3.utils.Util;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


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
		if (sender instanceof IUser)
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

		ISettings settings = ess.getSettings();
		final int prefixLength = FormatUtil.stripColor(settings.getData().getChat().getNicknamePrefix()).length();
		boolean foundPlayer = false;
		for (Player onlinePlayer : server.getOnlinePlayers())
		{
			final IUser user = ess.getUserMap().getUser(onlinePlayer);

			if (user.isHidden() && !showhidden)
			{
				continue;
			}
			final String nickName = FormatUtil.stripFormat(user.getData().getNickname());
			if (!whois.equalsIgnoreCase(nickName)
				&& !whois.substring(prefixLength).equalsIgnoreCase(nickName)
				&& !whois.equalsIgnoreCase(user.getName()))
			{
				continue;
			}
			foundPlayer = true;
			sender.sendMessage(_("whoisTop", user.getName()));
			user.setDisplayNick();
			sender.sendMessage(_("whoisIs", user.getPlayer().getDisplayName(), user.getName()));
			sender.sendMessage(_("whoisHealth", user.getPlayer().getHealth()));
			sender.sendMessage(_("whoisExp", SetExpFix.getTotalExperience(user.getPlayer()), user.getPlayer().getLevel()));
			sender.sendMessage(_("whoisLocation", user.getPlayer().getLocation().getWorld().getName(), user.getPlayer().getLocation().getBlockX(), user.getPlayer().getLocation().getBlockY(), user.getPlayer().getLocation().getBlockZ()));
			sender.sendMessage(_("whoisMoney", FormatUtil.displayCurrency(user.getMoney(), ess)));
			sender.sendMessage(_("whoisIPAddress", user.getPlayer().getAddress().getAddress().toString()));
			final String location = user.getData().getGeolocation();
			if (location != null
				&& Permissions.GEOIP_SHOW.isAuthorized(sender))
			{
				sender.sendMessage(_("whoisGeoLocation", location));
			}
			sender.sendMessage(_("whoisGamemode", _(user.getPlayer().getGameMode().toString().toLowerCase(Locale.ENGLISH))));
			sender.sendMessage(_("whoisGod", (user.isGodModeEnabled() ? _("true") : _("false"))));
			sender.sendMessage(_("whoisOP", (user.isOp() ? _("true") : _("false"))));
			sender.sendMessage(_("whoisFly", user.getPlayer().getAllowFlight() ? _("true") : _("false"), user.getPlayer().isFlying() ? _("flying") : _("notFlying")));
			sender.sendMessage(_("whoisAFK", (user.getData().isAfk() ? _("true") : _("false"))));
			sender.sendMessage(_("whoisJail", (user.getData().isJailed()
											   ? user.getTimestamp(UserData.TimestampType.JAIL) > 0
												 ? DateUtil.formatDateDiff(user.getTimestamp(UserData.TimestampType.JAIL))
												 : _("true")
											   : _("false"))));
			sender.sendMessage(_("whoisMute", (user.getData().isMuted()
											   ? user.getTimestamp(UserData.TimestampType.MUTE) > 0
												 ? DateUtil.formatDateDiff(user.getTimestamp(UserData.TimestampType.MUTE))
												 : _("true")
											   : _("false"))));

			if (!foundPlayer)
			{
				throw new NoSuchFieldException(_("playerNotFound"));
			}
		}
	}
}
