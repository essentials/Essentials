package com.earth2me.essentials.commands;

import com.earth2me.essentials.CommandSource;
import com.earth2me.essentials.Console;
import static com.earth2me.essentials.I18n.tl;
import com.earth2me.essentials.User;
import com.earth2me.essentials.utils.FormatUtil;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class Commandhelpop extends EssentialsCommand
{
	public Commandhelpop()
	{
		super("helpop");
	}

	@Override
	public void run(final Server server, final User user, final String commandLabel, final String[] args) throws Exception
	{
		user.setDisplayNick();
		sendMessage(server, user.getSource(), user.getDisplayName(), args);
	}

	@Override
	public void run(final Server server, final CommandSource sender, final String commandLabel, final String[] args) throws Exception
	{
		sendMessage(server, sender, Console.NAME, args);
	}

	private void sendMessage(final Server server, final CommandSource sender, final String from, final String[] args) throws Exception
	{
		if (args.length < 1)
		{
			throw new NotEnoughArgumentsException();
		}
		ess.broadcastMessage("essentials.helpop.receive", tl("helpOp", from, FormatUtil.stripFormat(getFinalArg(args, 0)));
	}
}
