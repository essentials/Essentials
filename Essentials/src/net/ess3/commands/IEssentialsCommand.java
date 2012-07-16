package net.ess3.commands;

import net.ess3.api.IEssentials;
import net.ess3.api.IEssentialsModule;
import net.ess3.api.IPermission;
import net.ess3.api.IUser;
import net.ess3.api.server.CommandSender;
import org.bukkit.command.Command;


public interface IEssentialsCommand extends IPermission
{
	void run(IUser user, Command cmd, String commandLabel, String[] args)
			throws Exception;

	void run(CommandSender sender, Command cmd, String commandLabel, String[] args)
			throws Exception;

	void init(IEssentials ess, String commandLabel);

	void setEssentialsModule(IEssentialsModule module);
}
