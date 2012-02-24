package com.earth2me.essentials.components.commands.handlers;

import com.earth2me.essentials.Util;
import com.earth2me.essentials.components.commands.EssentialsCommand;
import org.bukkit.command.CommandSender;


public class Commandjails extends EssentialsCommand
{
	@Override
	protected void run(final CommandSender sender, final String commandLabel, final String[] args) throws Exception
	{
		sender.sendMessage("§7" + Util.joinList(" ", getContext().getJails().getList()));
	}
}
