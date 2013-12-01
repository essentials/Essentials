package net.ess3.commands;

import net.ess3.api.IUser;
import net.ess3.utils.FormatUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static net.ess3.I18n._;


public class Commandbroadcast extends EssentialsCommand
{
	@Override
	public void run(final IUser user, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 1)
		{
			throw new NotEnoughArgumentsException();
		}
		ess.broadcastMessage(null, _("§r§6[§4Broadcast§6]§a {0}", FormatUtil.replaceFormat(getFinalArg(args, 0)), user.getPlayer().getDisplayName()));
	}

	@Override
	public void run(final CommandSender sender, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 1)
		{
			throw new NotEnoughArgumentsException();
		}
		ess.broadcastMessage(null, _("§r§6[§4Broadcast§6]§a {0}", FormatUtil.replaceFormat(getFinalArg(args, 0)), sender.getName()));
	}
}
