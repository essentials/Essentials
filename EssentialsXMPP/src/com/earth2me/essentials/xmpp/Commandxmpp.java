package com.earth2me.essentials.xmpp;

import com.earth2me.essentials.components.commands.EssentialsCommand;
import com.earth2me.essentials.components.commands.NotEnoughArgumentsException;
import com.earth2me.essentials.components.messenger.Console;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class Commandxmpp extends EssentialsCommand
{
	@Override
	protected void run(final CommandSender sender, final String commandLabel, final String[] args) throws NotEnoughArgumentsException
	{
		if (args.length < 2)
		{
			throw new NotEnoughArgumentsException();
		}

		final String address = EssentialsXmpp.getInstance().getAddress(args[0]);
		if (address == null)
		{
			sender.sendMessage("§cThere are no players matching that name.");
		}
		else
		{
			final String message = getFinalArg(args, 1);
			final String senderName = sender instanceof Player ? getContext().getUser((Player)sender).getDisplayName() : Console.NAME;
			sender.sendMessage("[" + senderName + ">" + address + "] " + message);
			if (!EssentialsXmpp.getInstance().sendMessage(address, "[" + senderName + "] " + message))
			{
				sender.sendMessage("§cError sending message.");
			}
		}
	}
}
