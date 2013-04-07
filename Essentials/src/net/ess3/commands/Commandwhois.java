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
			sender.sendMessage(_("§6 ====== WhoIs:§c {0} §6======", user.getName()));
			sender.sendMessage(_("whoisIs", player.getDisplayName(), user.getName()));
			sender.sendMessage(_("§6 - Health:§r {0}/20", player.getHealth()));
			sender.sendMessage(_("§6 - Exp:§r {0} (Level {1})", SetExpFix.getTotalExperience(player), player.getLevel()));
			sender.sendMessage(
					_(
					"whoisLocation", player.getLocation().getWorld().getName(), player.getLocation().getBlockX(), player.getLocation().getBlockY(),
					player.getLocation().getBlockZ()));
			sender.sendMessage(_("§6 - Money:§r {0}", FormatUtil.displayCurrency(user.getMoney(), ess)));
			sender.sendMessage(_("§6 - IP Address:§r {0}", player.getAddress().getAddress().toString()));
			final String location = userData.getGeolocation();
			if (location != null && Permissions.GEOIP_SHOW.isAuthorized(sender))
			{
				sender.sendMessage(_("§6 - Location:§r {0}", location));
			}
			sender.sendMessage(_("§6 - Gamemode:§r {0}", _(player.getGameMode().toString().toLowerCase(Locale.ENGLISH))));
			sender.sendMessage(_("§6 - God mode:§r {0}", (user.isGodModeEnabled() ? _("§atrue§r") : _("§4false§r"))));
			sender.sendMessage(_("whoisOP", (user.isOp() ? _("§atrue§r") : _("§4false§r"))));
			sender.sendMessage(_("§6 - Fly mode:§r {0} ({1})", player.getAllowFlight() ? _("§atrue§r") : _("§4false§r"), player.isFlying() ? _("flying") : _("not flying")));
			sender.sendMessage(_("§6 - AFK:§r {0}", (userData.isAfk() ? _("§atrue§r") : _("§4false§r"))));
			sender.sendMessage(
					_(
					"whoisJail", (userData.isJailed() ? user.getTimestamp(UserData.TimestampType.JAIL) > 0 ? DateUtil.formatDateDiff(
								  user.getTimestamp(UserData.TimestampType.JAIL)) : _("§atrue§r") : _("§4false§r"))));
			sender.sendMessage(
					_(
					"whoisMute", (userData.isMuted() ? user.getTimestamp(UserData.TimestampType.MUTE) > 0 ? DateUtil.formatDateDiff(
								  user.getTimestamp(UserData.TimestampType.MUTE)) : _("§atrue§r") : _("§4false§r"))));
		}
		if (!foundPlayer)
		{
			throw new NoSuchFieldException(_("§4Player not found."));
		}
	}
}
