package net.ess3.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import net.ess3.utils.Util;


public class Commandjails extends EssentialsCommand
{
	@Override
	protected void run(final CommandSender sender, final String commandLabel, final String[] args) throws Exception
	{
		sender.sendMessage(ChatColor.GRAY + Util.joinList(" ", ess.getJails().getList()));
	}
}
