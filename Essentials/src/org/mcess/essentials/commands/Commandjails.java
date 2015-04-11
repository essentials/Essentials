package org.mcess.essentials.commands;

import org.mcess.essentials.CommandSource;
import org.mcess.essentials.utils.StringUtil;
import org.bukkit.Server;


public class Commandjails extends EssentialsCommand
{
	public Commandjails()
	{
		super("jails");
	}

	@Override
	protected void run(final Server server, final CommandSource sender, final String commandLabel, final String[] args) throws Exception
	{
		sender.sendMessage("§7" + StringUtil.joinList(" ", ess.getJails().getList()));
	}
}
