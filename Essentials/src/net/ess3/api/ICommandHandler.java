package net.ess3.api;

import java.util.Map;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.plugin.Plugin;


public interface ICommandHandler extends IReload, TabExecutor
{
	Map<String, String> disabledCommands();

	public void removePlugin(Plugin plugin);

	public void addPlugin(Plugin plugin);

	void showCommandError(CommandSender sender, String commandLabel, Throwable exception);
}
