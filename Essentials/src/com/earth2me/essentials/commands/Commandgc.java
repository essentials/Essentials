package com.earth2me.essentials.commands;

import static com.earth2me.essentials.I18n._;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.command.CommandSender;


public class Commandgc extends EssentialsCommand
{
	@Override
	protected void run(final CommandSender sender, final String commandLabel, final String[] args) throws Exception
	{
		float tps = ess.getTimer().getAverageTPS();
		ChatColor color;
		if (tps >= 18)
		{
			color = ChatColor.GREEN;
		}
		else if (tps >= 15)
		{
			color = ChatColor.YELLOW;
		}
		else
		{
			color = ChatColor.RED;
		}
		sender.sendMessage(_("tps", "" + color + tps));
		sender.sendMessage(_("gcmax", (Runtime.getRuntime().maxMemory() / 1024 / 1024)));
		sender.sendMessage(_("gctotal", (Runtime.getRuntime().totalMemory() / 1024 / 1024)));
		sender.sendMessage(_("gcfree", (Runtime.getRuntime().freeMemory() / 1024 / 1024)));

		for (World w : server.getWorlds())
		{
			sender.sendMessage(
					(w.getEnvironment() == World.Environment.NETHER ? "Nether" : "World") + " \"" + w.getName() + "\": "
					+ w.getLoadedChunks().length + _("gcchunks")
					+ w.getEntities().size() + _("gcentities"));
		}
	}
}
