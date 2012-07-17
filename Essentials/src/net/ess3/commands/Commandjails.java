package net.ess3.commands;

import net.ess3.api.server.CommandSender;
import net.ess3.utils.Util;


public class Commandjails extends EssentialsCommand
{
	@Override
	protected void run(final CommandSender sender, final String commandLabel, final String[] args) throws Exception
	{
		sender.sendMessage("§7" + Util.joinList(" ", ess.getJails().getList()));
	}
}
