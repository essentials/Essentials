package com.earth2me.essentials.components.commands.handlers;

import static com.earth2me.essentials.components.i18n.I18nComponent._;
import com.earth2me.essentials.Util;
import com.earth2me.essentials.api.ISettingsComponent;
import com.earth2me.essentials.components.commands.EssentialsCommand;
import com.earth2me.essentials.components.commands.NotEnoughArgumentsException;
import com.earth2me.essentials.components.users.IUser;
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
		final ISettingsComponent settings = getContext().getSettings();
		final String whois = args[0].toLowerCase(Locale.ENGLISH);
		for (Player onlinePlayer : getServer().getOnlinePlayers())
		{
			final IUser u = getContext().getUser(onlinePlayer);
			if (u.isHidden())
			{
				continue;
			}
			final String displayName = Util.stripColor(u.getDisplayName()).toLowerCase(Locale.ENGLISH);
			settings.acquireReadLock();
			if (!whois.equals(displayName)
				&& !displayName.equals(Util.stripColor(settings.getData().getChat().getNicknamePrefix()) + whois)
				&& !whois.equalsIgnoreCase(u.getName()))
			{
				continue;
			}
			sender.sendMessage(u.getDisplayName() + " " + _("is") + " " + u.getName());
		}
	}
}
