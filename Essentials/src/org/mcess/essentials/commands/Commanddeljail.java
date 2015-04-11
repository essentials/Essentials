package org.mcess.essentials.commands;

import org.mcess.essentials.CommandSource;
import static org.mcess.essentials.I18n.tl;
import org.bukkit.Server;


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
		
		ess.getJails().removeJail(args[0]);
		sender.sendMessage(tl("deleteJail", args[0]));
	}
}
