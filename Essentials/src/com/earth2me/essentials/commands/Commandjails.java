package com.earth2me.essentials.commands;

import com.earth2me.essentials.CommandSource;
import com.earth2me.essentials.utils.StringUtil;
import org.bukkit.Server;

import java.util.Collection;

import static com.earth2me.essentials.I18n._;


public class Commandjails extends EssentialsCommand
{
	public Commandjails()
	{
		super("jails");
	}

	@Override
	protected void run(final Server server, final CommandSource sender, final String commandLabel, final String[] args) throws Exception
	{
		Collection<String> jails = ess.getJails().getList();

		if (jails.size() == 0)
		{
			sender.sendMessage(_("noJails"));
		}
		else
		{
			sender.sendMessage(_("jails", jails.size(), StringUtil.joinList(" ", jails)));
		}
	}
}
