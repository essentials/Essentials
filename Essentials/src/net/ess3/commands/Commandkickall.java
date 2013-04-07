package net.ess3.commands;

import static net.ess3.I18n._;
import net.ess3.api.IUser;
import net.ess3.utils.FormatUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class Commandkickall extends EssentialsCommand
{
	@Override
	protected void run(final CommandSender sender, final String commandLabel, final String[] args) throws Exception
	{
		String kickReason = args.length > 1 ? getFinalArg(args, 1) : _("Kicked from server.");
		kickReason = FormatUtil.replaceFormat(kickReason.replace("\\n", "\n").replace("|", "\n"));
		for (Player onlinePlayer : server.getOnlinePlayers())
		{
			if (!(sender instanceof IUser && onlinePlayer.getName().equalsIgnoreCase(sender.getName())))
			{
				onlinePlayer.kickPlayer(kickReason);
			}
		}
	}
}
