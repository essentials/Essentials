package net.ess3.commands;

import static net.ess3.I18n._;
import net.ess3.api.IUser;
import lombok.Cleanup;
import org.bukkit.command.CommandSender;


public class Commandbanip extends EssentialsCommand
{
	@Override
	public void run(final CommandSender sender, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 1)
		{
			throw new NotEnoughArgumentsException();
		}

		@Cleanup
		final IUser player = ess.getUser(args[0]);
		player.acquireReadLock();

		if (player == null)
		{
			ess.getServer().banIP(args[0]);
			sender.sendMessage(_("banIpAddress"));
		}
		else
		{
			final String ipAddress = player.getData().getIpAddress();
			if (ipAddress.length() == 0)
			{
				throw new Exception(_("playerNotFound"));
			}
			ess.getServer().banIP(player.getData().getIpAddress());
			sender.sendMessage(_("banIpAddress"));
		}
	}
}
