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
		boolean foundUser = false;
		Player player = sender instanceof IUser ? ((IUser)sender).getPlayer() : null;
		for (Player onlinePlayer : server.getOnlinePlayers())
		{
			final IUser u = ess.getUserMap().getUser(onlinePlayer);
			if (player != null && !player.canSee(onlinePlayer))
			{
				continue;
			}
			final String displayName = FormatUtil.stripFormat(u.getPlayer().getDisplayName()).toLowerCase(Locale.ENGLISH);
			if (displayName.contains(whois))
			{
				foundUser = true;
				sender.sendMessage(u.getPlayer().getDisplayName() + " " + _("is") + " " + u.getName());
			}
		}
		if (!foundUser)
		{
			throw new NoSuchFieldException(_("playerNotFound"));
		}
	}
}
