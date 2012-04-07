package com.earth2me.essentials.commands;

import com.earth2me.essentials.api.server.ICommandSender;
import com.earth2me.essentials.utils.Util;
import org.bukkit.command.CommandSender;


public class Commandjails extends EssentialsCommand
{
	@Override
	protected void run(final ICommandSender sender, final String commandLabel, final String[] args) throws Exception
	{
		sender.sendMessage("§7" + Util.joinList(" ", ess.getJails().getList()));
	}
}
