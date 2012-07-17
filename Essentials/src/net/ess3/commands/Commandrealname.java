package net.ess3.commands;

import java.util.Locale;
import lombok.Cleanup;
import static net.ess3.I18n._;
import net.ess3.api.ISettings;
import net.ess3.api.IUser;
import net.ess3.api.server.CommandSender;
import net.ess3.api.server.Player;
import net.ess3.utils.Util;


public class Commandrealname extends EssentialsCommand
{
	@Override
	protected void run(final CommandSender sender, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 1)
		{
			throw new NotEnoughArgumentsException();
		}
		@Cleanup 
		final ISettings settings = ess.getSettings();
		final String whois = args[0].toLowerCase(Locale.ENGLISH);
		for (Player onlinePlayer : server.getOnlinePlayers())
		{
			final IUser u = player.getUser(onlinePlayer);
			if (u.isHidden())
			{
				continue;
			}
			u.setDisplayNick();
			final String displayName = Util.stripFormat(u.getDisplayName()).toLowerCase(Locale.ENGLISH);
			settings.acquireReadLock();
			if (!whois.equals(displayName)
				&& !displayName.equals(Util.stripFormat(settings.getData().getChat().getNicknamePrefix()) + whois)
				&& !whois.equalsIgnoreCase(u.getName()))
			{
				continue;
			}
			sender.sendMessage(u.getDisplayName() + " " + _("is") + " " + u.getName());
		}
	}
}
