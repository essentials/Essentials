package net.ess3.commands;

import java.util.Locale;
import static net.ess3.I18n._;
import net.ess3.api.ISettings;
import net.ess3.api.IUser;
import net.ess3.utils.FormatUtil;
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
		final ISettings settings = ess.getSettings();
		final String whois = args[0].toLowerCase(Locale.ENGLISH);
		Player player = sender instanceof IUser ? ((IUser)sender).getPlayer() : null;
		for (Player onlinePlayer : server.getOnlinePlayers())
		{
			final IUser u = ess.getUserMap().getUser(onlinePlayer);
			if (player != null && !player.canSee(onlinePlayer))
			{
				continue;
			}
			u.setDisplayNick();
			final String displayName = FormatUtil.stripFormat(u.getPlayer().getDisplayName()).toLowerCase(Locale.ENGLISH);
			if (!whois.equals(displayName)
				&& !displayName.equals(FormatUtil.stripFormat(settings.getData().getChat().getNicknamePrefix()) + whois)
				&& !whois.equalsIgnoreCase(u.getName()))
			{
				continue;
			}
			sender.sendMessage(u.getPlayer().getDisplayName() + " " + _("is") + " " + u.getName());
		}
	}
}
