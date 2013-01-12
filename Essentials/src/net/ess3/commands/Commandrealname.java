package net.ess3.commands;

import java.util.Locale;
import static net.ess3.I18n._;
import net.ess3.api.IUser;
import net.ess3.api.IUserMap;
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
		final String whois = args[0].toLowerCase(Locale.ENGLISH);
		boolean foundUser = false;
		final Player player = sender instanceof IUser ? ((IUser)sender).getPlayer() : null;
		final IUserMap userMap = ess.getUserMap();
		for (Player onlinePlayer : server.getOnlinePlayers())
		{
			final IUser u = userMap.getUser(onlinePlayer);
			if (player != null && !player.canSee(onlinePlayer))
			{
				continue;
			}
			final Player realPlayer = u.getPlayer();
			final String displayName = FormatUtil.stripFormat(realPlayer.getDisplayName()).toLowerCase(Locale.ENGLISH);
			if (displayName.contains(whois))
			{
				foundUser = true;
				sender.sendMessage(realPlayer.getDisplayName() + " " + _("is") + " " + u.getName());
			}
		}
		if (!foundUser)
		{
			throw new NoSuchFieldException(_("playerNotFound"));
		}
	}
}
