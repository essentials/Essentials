package com.earth2me.essentials.commands;

import static com.earth2me.essentials.I18n._;
import com.earth2me.essentials.economy.Trade;
import com.earth2me.essentials.utils.Util;
import com.earth2me.essentials.api.IUser;
import com.earth2me.essentials.permissions.KitPermissions;
import com.earth2me.essentials.settings.Kit;
import java.util.Collection;
import java.util.Locale;
import org.bukkit.command.CommandSender;


public class Commandkit extends EssentialsCommand
{
	@Override
	public void run(final IUser user, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 1)
		{
			Collection<String> kitList = ess.getKits().getList();
			if (kitList.isEmpty())
			{
				user.sendMessage(_("noKits"));
			}
			else
			{
				for (String kitName : kitList)
				{
					if (!KitPermissions.getPermission(kitName).isAuthorized(user))
					{
						kitList.remove(kitName);
					}
				}
				user.sendMessage(_("kits", Util.joinList(kitList)));
			}
			throw new NoChargeException();
		}
		else if (args.length > 1 && user.isAuthorized("essentials.kit.others"))
		{
			final User userTo = getPlayer(server, args, 1, true);
			final String kitName = Util.sanitizeString(args[0].toLowerCase(Locale.ENGLISH));
			giveKit(userTo, user, kitName);
		}
		else
		{
			final String kitName = Util.sanitizeString(args[0].toLowerCase(Locale.ENGLISH));
			giveKit(user, user, kitName);
		}
	}

	@Override
	public void run(final Server server, final CommandSender sender, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 2)
		{
			listKits(sender);
			throw new NoChargeException();
		}
		else
		{
			final User userTo = getPlayer(server, args, 1, true);
			final String kitName = args[0].toLowerCase(Locale.ENGLISH);
			final Kit kit = ess.getKits().getKit(kitName);

			if (!KitPermissions.getPermission(kitName).isAuthorized(user))
			{
				throw new Exception(_("noKitPermission", "essentials.kit." + kitName));
			}

			//TODO: Check kit delay
			sender.sendMessage(_("kitGive", kitName));
		}
	}

	private void listKits(CommandSender sender) throws Exception
	{
		final String kitList = Kit.listKits(ess, null);
		if (kitList.length() > 0)
		{
			sender.sendMessage(_("kits", kitList));
		}
		else
		{
			sender.sendMessage(_("noKits"));
		}
	}

	private void giveKit(User userTo, User userFrom, String kitName) throws Exception
	{
		final Map<String, Object> kit = ess.getSettings().getKit(kitName);

		if (!userFrom.isAuthorized("essentials.kit." + kitName))
		{
			throw new Exception(_("noKitPermission", "essentials.kit." + kitName));
		}

		final List<String> items = Kit.getItems(userTo, kit);

		Kit.checkTime(userFrom, kitName, kit);

		final Trade charge = new Trade("kit-" + kitName, ess);
		charge.isAffordableFor(userFrom);

		Kit.expandItems(ess, userTo, items);

		charge.charge(userFrom);
		userTo.sendMessage(_("kitGive", kitName));
	}
}
