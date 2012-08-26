package net.ess3.commands;

import lombok.Cleanup;
import static net.ess3.I18n._;
import net.ess3.api.IUser;
import org.bukkit.command.CommandSender;


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
			@Cleanup
			final IUser user = ess.getUserMap().matchUser(args[0], false, true);
			user.acquireReadLock();
			ess.getServer().unbanIP(user.getData().getIpAddress());
		}
		catch (Exception ex)
		{
		}
		ess.getServer().unbanIP(args[0]);
		sender.sendMessage(_("unbannedIP"));
	}
}
