package com.earth2me.essentials.components.commands.handlers;

import com.earth2me.essentials.Util;
import com.earth2me.essentials.api.ISettingsComponent;
import com.earth2me.essentials.components.commands.EssentialsCommand;
import com.earth2me.essentials.components.commands.NotEnoughArgumentsException;
import static com.earth2me.essentials.components.i18n.I18nComponent.$;
import com.earth2me.essentials.components.users.IUserComponent;
import com.earth2me.essentials.perm.Permissions;
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
		ISettingsComponent settings = getContext().getSettings();
		settings.acquireReadLock();
		final int prefixLength = Util.stripColor(settings.getData().getChat().getNicknamePrefix()).length();
		for (Player onlinePlayer : getServer().getOnlinePlayers())
		{
			@Cleanup
			final IUserComponent user = getContext().getUser(onlinePlayer);

			if (user.isHidden() && !showhidden)
			{
				continue;
			}
			user.acquireReadLock();
			final String nickName = Util.stripColor(user.getData().getNickName());
			if (!whois.equalsIgnoreCase(nickName)
				&& !whois.substring(prefixLength).equalsIgnoreCase(nickName)
				&& !whois.equalsIgnoreCase(user.getName()))
			{
				continue;
			}
			sender.sendMessage("");
			sender.sendMessage($("whoisIs", user.getDisplayName(), user.getName()));
			sender.sendMessage($("whoisHealth", user.getHealth()));
			sender.sendMessage($("whoisOP", (user.isOp() ? $("true") : $("false"))));
			sender.sendMessage($("whoisGod", (user.isGodModeEnabled() ? $("true") : $("false"))));
			sender.sendMessage($("whoisGamemode", $(user.getGameMode().toString().toLowerCase(Locale.ENGLISH))));
			sender.sendMessage($("whoisLocation", user.getLocation().getWorld().getName(), user.getLocation().getBlockX(), user.getLocation().getBlockY(), user.getLocation().getBlockZ()));
			sender.sendMessage($("whoisMoney", Util.formatCurrency(user.getMoney(), getContext())));
			sender.sendMessage(user.getData().isAfk()
							   ? $("whoisStatusAway")
							   : $("whoisStatusAvailable"));
			sender.sendMessage($("whoisIPAddress", user.getAddress().getAddress().toString()));
			final String location = user.getData().getGeoLocation();
			if (location != null
				&& Permissions.GEOIP_SHOW.isAuthorized(sender))
			{
				sender.sendMessage($("whoisGeoLocation", location));
			}
		}
	}
}
