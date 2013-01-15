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

		final ISettings settings = ess.getSettings();
		final int prefixLength = FormatUtil.stripColor(settings.getData().getChat().getNicknamePrefix()).length();
		boolean foundPlayer = false;
		for (Player onlinePlayer : server.getOnlinePlayers())
		{
			final IUser user = ess.getUserMap().getUser(onlinePlayer);

			if (sender instanceof IUser && ((IUser)sender).getPlayer().canSee(onlinePlayer) && !showhidden)
			{
				continue;
			}
			final UserData userData = user.getData();
			final String nickName = FormatUtil.stripFormat(userData.getNickname());
			if (!whois.equalsIgnoreCase(nickName) && !whois.substring(prefixLength).equalsIgnoreCase(nickName) && !whois.equalsIgnoreCase(user.getName()))
			{
				continue;
			}
			final Player player = user.getPlayer();
			foundPlayer = true;
			sender.sendMessage(_("whoisTop", user.getName()));
			sender.sendMessage(_("whoisIs", player.getDisplayName(), user.getName()));
			sender.sendMessage(_("whoisHealth", player.getHealth()));
			sender.sendMessage(_("whoisExp", SetExpFix.getTotalExperience(player), player.getLevel()));
			sender.sendMessage(
					_(
							"whoisLocation", player.getLocation().getWorld().getName(), player.getLocation().getBlockX(), player.getLocation().getBlockY(),
							player.getLocation().getBlockZ()));
			sender.sendMessage(_("whoisMoney", FormatUtil.displayCurrency(user.getMoney(), ess)));
			sender.sendMessage(_("whoisIPAddress", player.getAddress().getAddress().toString()));
			final String location = userData.getGeolocation();
			if (location != null && Permissions.GEOIP_SHOW.isAuthorized(sender))
			{
				sender.sendMessage(_("whoisGeoLocation", location));
			}
			sender.sendMessage(_("whoisGamemode", _(player.getGameMode().toString().toLowerCase(Locale.ENGLISH))));
			sender.sendMessage(_("whoisGod", (user.isGodModeEnabled() ? _("true") : _("false"))));
			sender.sendMessage(_("whoisOP", (user.isOp() ? _("true") : _("false"))));
			sender.sendMessage(_("whoisFly", player.getAllowFlight() ? _("true") : _("false"), player.isFlying() ? _("flying") : _("notFlying")));
			sender.sendMessage(_("whoisAFK", (userData.isAfk() ? _("true") : _("false"))));
			sender.sendMessage(
					_(
							"whoisJail", (userData.isJailed() ? user.getTimestamp(UserData.TimestampType.JAIL) > 0 ? DateUtil.formatDateDiff(
							user.getTimestamp(UserData.TimestampType.JAIL)) : _("true") : _("false"))));
			sender.sendMessage(
					_(
							"whoisMute", (userData.isMuted() ? user.getTimestamp(UserData.TimestampType.MUTE) > 0 ? DateUtil.formatDateDiff(
							user.getTimestamp(UserData.TimestampType.MUTE)) : _("true") : _("false"))));

			if (!foundPlayer)
			{
				throw new NoSuchFieldException(_("playerNotFound"));
			}
		}
	}
}
