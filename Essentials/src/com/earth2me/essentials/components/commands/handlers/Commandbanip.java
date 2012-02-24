package com.earth2me.essentials.components.commands.handlers;

import com.earth2me.essentials.components.commands.EssentialsCommand;
import com.earth2me.essentials.components.commands.NotEnoughArgumentsException;
import static com.earth2me.essentials.I18nComponent._;
import com.earth2me.essentials.components.users.IUser;
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
		final IUser player = getContext().getUser(args[0]);
		player.acquireReadLock();

		if (player == null)
		{
			getContext().getServer().banIP(args[0]);
			sender.sendMessage(_("banIpAddress"));
		}
		else
		{
			final String ipAddress = player.getData().getIpAddress();
			if (ipAddress.length() == 0)
			{
				throw new Exception(_("playerNotFound"));
			}
			getContext().getServer().banIP(player.getData().getIpAddress());
			sender.sendMessage(_("banIpAddress"));
		}
	}
}
