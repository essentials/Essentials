package com.earth2me.essentials.commands;

import static com.earth2me.essentials.I18n._;
import com.earth2me.essentials.User;
import com.earth2me.essentials.craftbukkit.SetExpFix;
import com.earth2me.essentials.utils.DateUtil;
import com.earth2me.essentials.utils.NumberUtil;
import java.util.Locale;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class Commandwhois extends EssentialsCommand
{
	public Commandwhois()
	{
		super("whois");
	}

	@Override
	public void run(final Server server, final CommandSender sender, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 1)
		{
			throw new NotEnoughArgumentsException();
		}

		User user = getPlayer(server, sender, args, 0);

		sender.sendMessage(_("whoisTop", user.getName()));
		user.setDisplayNick();
		sender.sendMessage(_("whoisNick", user.getDisplayName()));
		sender.sendMessage(_("whoisHealth", user.getHealth()));
		sender.sendMessage(_("whoisHunger", user.getFoodLevel(), user.getSaturation()));
		sender.sendMessage(_("whoisExp", SetExpFix.getTotalExperience(user), user.getLevel()));
		sender.sendMessage(_("whoisLocation", user.getLocation().getWorld().getName(), user.getLocation().getBlockX(), user.getLocation().getBlockY(), user.getLocation().getBlockZ()));
		if (!ess.getSettings().isEcoDisabled())
		{
			sender.sendMessage(_("whoisMoney", NumberUtil.displayCurrency(user.getMoney(), ess)));
		}
		sender.sendMessage(_("whoisIPAddress", user.getAddress().getAddress().toString()));
		final String location = user.getGeoLocation();
		if (location != null
			&& (sender instanceof Player ? ess.getUser(sender).isAuthorized("essentials.geoip.show") : true))
		{
			sender.sendMessage(_("whoisGeoLocation", location));
		}
		sender.sendMessage(_("whoisGamemode", _(user.getGameMode().toString().toLowerCase(Locale.ENGLISH))));
		sender.sendMessage(_("whoisGod", (user.isGodModeEnabled() ? _("true") : _("false"))));
		sender.sendMessage(_("whoisOp", (user.isOp() ? _("true") : _("false"))));
		sender.sendMessage(_("whoisFly", user.getAllowFlight() ? _("true") : _("false"), user.isFlying() ? _("flying") : _("notFlying")));
		sender.sendMessage(_("whoisAFK", (user.isAfk() ? _("true") : _("false"))));
		sender.sendMessage(_("whoisJail", (user.isJailed()
										   ? user.getJailTimeout() > 0
											 ? DateUtil.formatDateDiff(user.getJailTimeout())
											 : _("true")
										   : _("false"))));
		sender.sendMessage(_("whoisMuted", (user.isMuted()
											? user.getMuteTimeout() > 0
											  ? DateUtil.formatDateDiff(user.getMuteTimeout())
											  : _("true")
											: _("false"))));

	}
}
