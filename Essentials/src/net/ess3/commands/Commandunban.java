package net.ess3.commands;

import lombok.Cleanup;
import static net.ess3.I18n._;
import net.ess3.api.IUser;
import net.ess3.api.server.CommandSender;


public class Commandunban extends EssentialsCommand
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
			final IUser player = ess.getUserMap().matchUser(args[0], false, true);
			player.acquireWriteLock();
			player.getData().setBan(null);
			player.setBanned(false);
			sender.sendMessage(_("unbannedPlayer"));
		}
		catch (NoSuchFieldException e)
		{
			throw new Exception(_("playerNotFound"), e);
		}
	}
}
