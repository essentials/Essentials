package net.ess3.commands;

import java.util.Collection;
import java.util.Locale;
import static net.ess3.I18n._;
import net.ess3.api.IUser;
import net.ess3.economy.Trade;
import net.ess3.permissions.Permissions;
import net.ess3.settings.Kit;
import net.ess3.utils.Util;


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
				user.sendMessage(_("§6There are no kits available yet."));
			}
			else
			{
				for (String kitName : kitList)
				{
					if (!Permissions.KITS.isAuthorized(user, kitName))
					{
						kitList.remove(kitName);
					}
				}
				user.sendMessage(_("§6Kits:§r {0}", Util.joinList(kitList)));
			}
			throw new NoChargeException();
		}
		else
		{
			final String kitName = args[0].toLowerCase(Locale.ENGLISH);
			final Kit kit = ess.getKits().getKit(kitName);

			if (!Permissions.KITS.isAuthorized(user, kitName))
			{
				throw new Exception(_("§4You need the §c{0}§4 permission to use that kit.", "essentials.kit." + kitName));
			}

			//TODO: Check kit delay

			final Trade charge = new Trade("kit-" + kitName, ess);
			charge.isAffordableFor(user);

			ess.getKits().sendKit(user, kit);

			//TODO: Merge kit changes from 2.9

			charge.charge(user);
			user.sendMessage(_("§6Giving kit§c {0}§6.", kitName));

		}
	}
}
