package com.earth2me.essentials.components.commands.handlers;

import com.earth2me.essentials.components.commands.EssentialsCommand;
import com.earth2me.essentials.components.commands.NotEnoughArgumentsException;
import static com.earth2me.essentials.components.i18n.I18nComponent._;
import com.earth2me.essentials.components.users.IUser;
import lombok.Cleanup;
import org.bukkit.command.CommandSender;


public class Commandunban extends EssentialsCommand
{
	@Override
	public void run(final CommandSender sender, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 1)
		{
			throw new NotEnoughArgumentsException();
		}

		try
		{
			@Cleanup
			final IUser player = getPlayer(args, 0, true);
			player.acquireWriteLock();
			player.getData().setBan(null);
			player.setBanned(false);
			sender.sendMessage(_("unbannedPlayer"));
		}
		catch (NoSuchFieldException e)
		{
			throw new Exception(_("playerNotFound"));
		}
	}
}
