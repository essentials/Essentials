package org.mcess.essentials.commands;

import org.mcess.essentials.CommandSource;
import org.mcess.essentials.User;
import org.mcess.essentials.craftbukkit.SetExpFix;
import org.mcess.essentials.utils.DateUtil;
import org.mcess.essentials.utils.NumberUtil;
import java.util.Locale;
import org.bukkit.Server;
import org.mcess.essentials.I18n;


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

		sender.sendMessage(I18n.tl("whoisTop", user.getName()));
		user.setDisplayNick();
		sender.sendMessage(I18n.tl("whoisNick", user.getDisplayName()));
		sender.sendMessage(I18n.tl("whoisHealth", user.getBase().getHealth()));
		sender.sendMessage(I18n.tl("whoisHunger", user.getBase().getFoodLevel(), user.getBase().getSaturation()));
		sender.sendMessage(I18n.tl("whoisExp", SetExpFix.getTotalExperience(user.getBase()), user.getBase().getLevel()));
		sender.sendMessage(I18n.tl("whoisLocation", user.getLocation().getWorld().getName(), user.getLocation().getBlockX(), user.getLocation().getBlockY(), user.getLocation().getBlockZ()));
		if (!ess.getSettings().isEcoDisabled())
		{
			sender.sendMessage(I18n.tl("whoisMoney", NumberUtil.displayCurrency(user.getMoney(), ess)));
		}
		sender.sendMessage(I18n.tl("whoisIPAddress", user.getBase().getAddress().getAddress().toString()));
		final String location = user.getGeoLocation();
		if (location != null
			&& (sender.isPlayer() ? ess.getUser(sender.getPlayer()).isAuthorized("essentials.geoip.show") : true))
		{
			sender.sendMessage(I18n.tl("whoisGeoLocation", location));
		}
		sender.sendMessage(I18n.tl("whoisGamemode", I18n.tl(user.getBase().getGameMode().toString().toLowerCase(Locale.ENGLISH))));
		sender.sendMessage(I18n.tl("whoisGod", (user.isGodModeEnabled() ? I18n.tl("true") : I18n.tl("false"))));
		sender.sendMessage(I18n.tl("whoisOp", (user.getBase().isOp() ? I18n.tl("true") : I18n.tl("false"))));
		sender.sendMessage(I18n.tl("whoisFly", user.getBase().getAllowFlight() ? I18n.tl("true") : I18n.tl("false"), user.getBase().isFlying() ? I18n.tl("flying") : I18n.tl("notFlying")));
		sender.sendMessage(I18n.tl("whoisAFK", (user.isAfk() ? I18n.tl("true") : I18n.tl("false"))));
		sender.sendMessage(I18n.tl("whoisJail", (user.isJailed()
				? user.getJailTimeout() > 0
				? DateUtil.formatDateDiff(user.getJailTimeout())
				: I18n.tl("true")
				: I18n.tl("false"))));
		sender.sendMessage(I18n.tl("whoisMuted", (user.isMuted()
				? user.getMuteTimeout() > 0
				? DateUtil.formatDateDiff(user.getMuteTimeout())
				: I18n.tl("true")
				: I18n.tl("false"))));

	}
}
