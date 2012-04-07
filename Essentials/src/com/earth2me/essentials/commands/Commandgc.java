package com.earth2me.essentials.commands;

import static com.earth2me.essentials.I18n._;
import com.earth2me.essentials.api.server.ICommandSender;
import com.earth2me.essentials.api.server.IWorld;
import org.bukkit.World;
import org.bukkit.command.CommandSender;


public class Commandgc extends EssentialsCommand
{
	@Override
	protected void run(final ICommandSender sender, final String commandLabel, final String[] args) throws Exception
	{
		sender.sendMessage(_("gcmax", (Runtime.getRuntime().maxMemory() / 1024 / 1024)));
		sender.sendMessage(_("gctotal", (Runtime.getRuntime().totalMemory() / 1024 / 1024)));
		sender.sendMessage(_("gcfree", (Runtime.getRuntime().freeMemory() / 1024 / 1024)));

		for (IWorld w : server.getWorlds())
		{
			sender.sendMessage(
					(w.getEnvironment() == World.Environment.NETHER ? "Nether" : "World") + " \"" + w.getName() + "\": "
					+ w.getLoadedChunks().length + _("gcchunks")
					+ w.getEntities().size() + _("gcentities"));
		}
	}
}
