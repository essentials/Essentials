package com.earth2me.essentials.commands;

import static com.earth2me.essentials.I18n._;
import com.earth2me.essentials.utils.Util;
import com.earth2me.essentials.api.ISettings;
import com.earth2me.essentials.api.IUser;
import java.util.Locale;
import lombok.Cleanup;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


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
			final IUser u = ess.getUser(onlinePlayer);
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
