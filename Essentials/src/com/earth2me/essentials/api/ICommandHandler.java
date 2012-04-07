package com.earth2me.essentials.api;

import com.earth2me.essentials.api.server.ICommandSender;
import java.util.Map;
import org.bukkit.command.Command;
import org.bukkit.plugin.Plugin;


public interface ICommandHandler extends IReload
{
	Map<String, String> disabledCommands();

	public void removePlugin(Plugin plugin);

	public void addPlugin(Plugin plugin);

	boolean handleCommand(ICommandSender sender, Command command, String commandLabel, String[] args);

	void showCommandError(ICommandSender sender, String commandLabel, Throwable exception);
}
