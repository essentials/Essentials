package com.earth2me.essentials.components.commands;

import com.earth2me.essentials.api.IContext;
import com.earth2me.essentials.api.IPermissions;
import com.earth2me.essentials.components.IComponent;
import com.earth2me.essentials.components.users.IUserComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;


public interface IEssentialsCommand extends IPermissions
{
	void run(IUserComponent user, Command cmd, String commandLabel, String[] args)
			throws Exception;

	void run(CommandSender sender, Command cmd, String commandLabel, String[] args)
			throws Exception;

	void init(IContext ess, String commandLabel);

	void setComponent(IComponent component);
}
