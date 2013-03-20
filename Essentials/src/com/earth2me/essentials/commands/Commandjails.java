package com.earth2me.essentials.commands;

import com.earth2me.essentials.Util;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;


public class Commandjails extends EssentialsCommand
{
	private final static int JAILS_PER_PAGE = 20;

	
	public Commandjails()
	{
		super("jails");
	}

	@Override
	protected void run(final Server server, final CommandSender sender, final String commandLabel, final String[] args) throws Exception
	{
		final List<String> jailNameList = new ArrayList<String>(ess.getJails().getList()
		final String jailList;
		if (jailNameList.size() > JAILS_PER_PAGE)
		{
			int page = 1;
			if (args.length > 0 && Util.isInt(args[0]))
			{
				page = Integer.parseInt(args[0]);
			}
			final int jailPage = (page - 1) * JAILS_PER_PAGE;
			jailList = Util.joinList(jailNameList.subList(jailPage, jailPage + Math.min(jailNameList.size() - jailPage, JAILS_PER_PAGE)))
			sender.sendMessage(_("jailsCount", jailNameList.size(), page, (int)Math.ceil(jailNameList.size() / (double)JAILS_PER_PAGE)));
			sender.sendMessage(_("jailList", jailList));
		}
		else
		{
			jailList = Util.joinList(", ", jailNameList);
			sender.sendMessage(_("jails", jailList));
		}
	}
}
