package net.ess3.commands;

import static net.ess3.I18n._;
import org.bukkit.command.CommandSender;
import net.ess3.api.IUser;


public class Commandunban extends EssentialsCommand
{
	@Override
	protected void run(final CommandSender sender, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 1)
		{
			throw new NotEnoughArgumentsException();
		}


		final IUser player = ess.getUserMap().matchUser(args[0], true);
		player.getData().setBan(null);
		player.setBanned(false);
		player.queueSave();
		sender.sendMessage(_("unbannedPlayer"));
	}
}
