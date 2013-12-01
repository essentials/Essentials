package net.ess3.commands;

import net.ess3.utils.DateUtil;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

import java.lang.management.ManagementFactory;
import java.util.List;

import static net.ess3.I18n._;


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
		sender.sendMessage(_("§6Uptime:§c {0}", DateUtil.formatDateDiff(ManagementFactory.getRuntimeMXBean().getStartTime())));
		sender.sendMessage(_("§6Current TPS = {0}", "" + color + tps));
		sender.sendMessage(_("§6Maximum memory:§c {0} MB.", (runtime.maxMemory() / 1024 / 1024)));
		sender.sendMessage(_("§6Allocated memory:§c {0} MB.", (runtime.totalMemory() / 1024 / 1024)));
		sender.sendMessage(_("§6Free memory:§c {0} MB.", (runtime.freeMemory() / 1024 / 1024)));
		sender.sendMessage(_("gcquene", (ess.getStorageQueue().getQueueSize())));

		List<World> worlds = server.getWorlds();

		if(worlds.size() > 3 && args.length == 0)
		{
			sender.sendMessage(_("§4Message truncated, to see the full output type:§c /{0} all", commandLabel));
		}
		else
		{
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
						worldType + " \"" + w.getName() + "\": " + w.getLoadedChunks().length + _("gcchunks") + w.getEntities().size() + _("gcentities"));
			}
		}
	}
}
