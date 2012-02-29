package com.earth2me.essentials.components.commands.handlers;

import com.earth2me.essentials.components.commands.EssentialsCommand;
import static com.earth2me.essentials.components.i18n.I18nComponent.$;
import org.bukkit.World;
import org.bukkit.command.CommandSender;


public class Commandgc extends EssentialsCommand
{
	@Override
	protected void run(final CommandSender sender, final String commandLabel, final String[] args) throws Exception
	{
		sender.sendMessage($("gcmax", (Runtime.getRuntime().maxMemory() / 1024 / 1024)));
		sender.sendMessage($("gctotal", (Runtime.getRuntime().totalMemory() / 1024 / 1024)));
		sender.sendMessage($("gcfree", (Runtime.getRuntime().freeMemory() / 1024 / 1024)));

		for (World w : getServer().getWorlds())
		{
			sender.sendMessage(
					(w.getEnvironment() == World.Environment.NETHER ? "Nether" : "World") + " \"" + w.getName() + "\": "
					+ w.getLoadedChunks().length + $("gcchunks")
					+ w.getEntities().size() + $("gcentities"));
		}
	}
}
