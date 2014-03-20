package com.earth2me.essentials.commands;

import com.earth2me.essentials.CommandSource;
import static com.earth2me.essentials.I18n.tl_;
import com.earth2me.essentials.utils.DateUtil;
import com.earth2me.essentials.utils.NumberUtil;
import java.lang.management.ManagementFactory;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Server;
import org.bukkit.World;


public class Commandgc extends EssentialsCommand
{
	public Commandgc()
	{
		super("gc");
	}

	@Override
	protected void run(final Server server, final CommandSource sender, final String commandLabel, final String[] args) throws Exception
	{
		double tps = ess.getTimer().getAverageTPS();
		ChatColor color;
		if (tps >= 18.0)
		{
			color = ChatColor.GREEN;
		}
		else if (tps >= 15.0)
		{
			color = ChatColor.YELLOW;
		}
		else
		{
			color = ChatColor.RED;
		}

		sender.sendMessage(tl_("uptime", DateUtil.formatDateDiff(ManagementFactory.getRuntimeMXBean().getStartTime())));
		sender.sendMessage(tl_("tps", "" + color + NumberUtil.formatDouble(tps)));
		sender.sendMessage(tl_("gcmax", (Runtime.getRuntime().maxMemory() / 1024 / 1024)));
		sender.sendMessage(tl_("gctotal", (Runtime.getRuntime().totalMemory() / 1024 / 1024)));
		sender.sendMessage(tl_("gcfree", (Runtime.getRuntime().freeMemory() / 1024 / 1024)));

		List<World> worlds = server.getWorlds();
		for (World w : worlds)
		{
			String worldType = "World";
			switch (w.getEnvironment())
			{
			case NETHER:
				worldType = "Nether";
				break;
			case THE_END:
				worldType = "The End";
				break;
			}
			
			int tileEntities = 0;
			
			for (Chunk chunk : w.getLoadedChunks()) {
				tileEntities += chunk.getTileEntities().length;
			}

			sender.sendMessage(tl_("gcWorld", worldType, w.getName(), w.getLoadedChunks().length, w.getEntities().size(), tileEntities));
		}
	}
}
