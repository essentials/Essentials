package com.earth2me.essentials.components.commands.handlers;

import static com.earth2me.essentials.components.i18n.I18nComponent._;
import com.earth2me.essentials.Trade;
import com.earth2me.essentials.Util;
import com.earth2me.essentials.components.commands.EssentialsCommand;
import com.earth2me.essentials.components.commands.NoChargeException;
import com.earth2me.essentials.components.users.IUser;
import com.earth2me.essentials.perm.KitPermissions;
import com.earth2me.essentials.components.settings.Kit;
import java.util.Collection;
import java.util.Locale;


public class Commandkit extends EssentialsCommand
{
	@Override
	public void run(final IUser user, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 1)
		{
			Collection<String> kitList = getContext().getKits().getList();
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
		else
		{
			final String kitName = args[0].toLowerCase(Locale.ENGLISH);
			final Kit kit = getContext().getKits().getKit(kitName);

			if (!KitPermissions.getPermission(kitName).isAuthorized(user))
			{
				throw new Exception(_("noKitPermission", "essentials.kit." + kitName));
			}

			//TODO: Check kit delay

			final Trade charge = new Trade("kit-" + kitName, getContext());
			charge.isAffordableFor(user);

			getContext().getKits().sendKit(user, kit);

			charge.charge(user);
			user.sendMessage(_("kitGive", kitName));

		}
	}
}
