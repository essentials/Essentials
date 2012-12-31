package net.ess3.commands;

import java.lang.management.ManagementFactory;
import static net.ess3.I18n._;
import net.ess3.utils.DateUtil;
import org.bukkit.ChatColor;
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
		final Runtime runtime = Runtime.getRuntime();
		sender.sendMessage(_("uptime", DateUtil.formatDateDiff(ManagementFactory.getRuntimeMXBean().getStartTime())));
		sender.sendMessage(_("tps", "" + color + tps));
		sender.sendMessage(_("gcmax", (runtime.maxMemory() / 1024 / 1024)));
		sender.sendMessage(_("gctotal", (runtime.totalMemory() / 1024 / 1024)));
		sender.sendMessage(_("gcfree", (runtime.freeMemory() / 1024 / 1024)));
		sender.sendMessage(_("gcquene", (ess.getStorageQueue().getQueueSize()))); 

		for (World w : server.getWorlds())
		{
			final String worldType;
			switch (w.getEnvironment())
			{
			case NETHER:
				worldType = "Nether";
				break;
			case THE_END:
				worldType = "The End";
				break;
			default:
				worldType = "World";
				break;
				
			}

			sender.sendMessage(
					worldType + " \"" + w.getName() + "\": "
					+ w.getLoadedChunks().length + _("gcchunks")
					+ w.getEntities().size() + _("gcentities"));
		}
	}
}
