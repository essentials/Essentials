package com.earth2me.essentials.commands;

import com.earth2me.essentials.CommandSource;
import static com.earth2me.essentials.I18n.tl_;
import com.earth2me.essentials.User;
import com.earth2me.essentials.craftbukkit.SetExpFix;
import com.earth2me.essentials.utils.DateUtil;
import com.earth2me.essentials.utils.NumberUtil;
import java.util.Locale;
import org.bukkit.Server;


public class Commandwhois extends EssentialsCommand
{
	public Commandwhois()
	{
		super("whois");
	}

	@Override
	public void run(final Server server, final CommandSource sender, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 1)
		{
			throw new NotEnoughArgumentsException();
		}

		User user = getPlayer(server, sender, args, 0);

		sender.sendMessage(tl_("whoisTop", user.getName()));
		user.setDisplayNick();
		sender.sendMessage(tl_("whoisNick", user.getDisplayName()));
		sender.sendMessage(tl_("whoisHealth", user.getHealth()));
		sender.sendMessage(tl_("whoisHunger", user.getFoodLevel(), user.getSaturation()));
		sender.sendMessage(tl_("whoisExp", SetExpFix.getTotalExperience(user.getBase()), user.getLevel()));
		sender.sendMessage(tl_("whoisLocation", user.getLocation().getWorld().getName(), user.getLocation().getBlockX(), user.getLocation().getBlockY(), user.getLocation().getBlockZ()));
		if (!ess.getSettings().isEcoDisabled())
		{
			sender.sendMessage(tl_("whoisMoney", NumberUtil.displayCurrency(user.getMoney(), ess)));
		}
		sender.sendMessage(tl_("whoisIPAddress", user.getAddress().getAddress().toString()));
		final String location = user.getGeoLocation();
		if (location != null
			&& (sender.isPlayer() ? ess.getUser(sender.getPlayer()).isAuthorized("essentials.geoip.show") : true))
		{
			sender.sendMessage(tl_("whoisGeoLocation", location));
		}
		sender.sendMessage(tl_("whoisGamemode", tl_(user.getGameMode().toString().toLowerCase(Locale.ENGLISH))));
		sender.sendMessage(tl_("whoisGod", (user.isGodModeEnabled() ? tl_("true") : tl_("false"))));
		sender.sendMessage(tl_("whoisOp", (user.isOp() ? tl_("true") : tl_("false"))));
		sender.sendMessage(tl_("whoisFly", user.getAllowFlight() ? tl_("true") : tl_("false"), user.isFlying() ? tl_("flying") : tl_("notFlying")));
		sender.sendMessage(tl_("whoisAFK", (user.isAfk() ? tl_("true") : tl_("false"))));
		sender.sendMessage(tl_("whoisJail", (user.isJailed()
										   ? user.getJailTimeout() > 0
											 ? DateUtil.formatDateDiff(user.getJailTimeout())
											 : tl_("true")
										   : tl_("false"))));
		sender.sendMessage(tl_("whoisMuted", (user.isMuted()
											? user.getMuteTimeout() > 0
											  ? DateUtil.formatDateDiff(user.getMuteTimeout())
											  : tl_("true")
											: tl_("false"))));

	}
}
