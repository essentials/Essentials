package com.earth2me.essentials.commands;

import com.earth2me.essentials.CommandSource;
import org.bukkit.Server;

import static com.earth2me.essentials.I18n._;


public class Commanddeljail extends EssentialsCommand
{
	public Commanddeljail()
	{
		super("deljail");
	}

	@Override
	protected void run(final Server server, final CommandSource sender, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 1)
		{
			throw new NotEnoughArgumentsException();
		}

		if (ess.getJails().getList().contains(args[0]))
		{
			ess.getJails().removeJail(args[0]);
			sender.sendMessage(_("deleteJail", args[0]));
		}
		else
		{
			sender.sendMessage(_("jailNotExist"));
		}
	}
}
