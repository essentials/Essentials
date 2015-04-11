package org.mcess.essentials.commands;

import org.mcess.essentials.CommandSource;
import org.mcess.essentials.IEssentialsModule;
import org.mcess.essentials.User;
import net.ess3.api.IEssentials;
import org.bukkit.Server;
import org.bukkit.command.Command;


public interface IEssentialsCommand
{
	String getName();

	void run(Server server, User user, String commandLabel, Command cmd, String[] args)
			throws Exception;

	void run(Server server, CommandSource sender, String commandLabel, Command cmd, String[] args)
			throws Exception;

	void setEssentials(IEssentials ess);

	void setEssentialsModule(IEssentialsModule module);
}
