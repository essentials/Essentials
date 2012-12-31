package net.ess3.commands;

import static net.ess3.I18n._;
import org.bukkit.command.CommandSender;
import net.ess3.api.IUser;


public class Commandunbanip extends EssentialsCommand
{
	@Override
	protected void run(final CommandSender sender, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 1)
		{
			throw new NotEnoughArgumentsException();
		}
		try
		{
			final IUser user = ess.getUserMap().matchUser(args[0], true);
			ess.getServer().unbanIP(user.getData().getIpAddress());
		}
		catch (Exception ex)
		{
		}
		ess.getServer().unbanIP(args[0]);
		sender.sendMessage(_("unbannedIP"));
	}
}
