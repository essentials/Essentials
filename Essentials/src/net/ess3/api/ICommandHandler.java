package net.ess3.api;

import java.util.Map;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.plugin.Plugin;


public interface ICommandHandler extends IReload, TabExecutor
{
	/**
	 * Returns a map of disabled commands and the alternate command found
	 * String one is the name of the disabled command
	 * String two is the alternate that was found
	 * @return
	 */
	Map<String, String> disabledCommands();

	/**
	 *
	 * @param plugin the plugin to add
	 */
	void removePlugin(Plugin plugin);

	/**
	 *
	 * @param plugin the plugin to remove
	 */
	void addPlugin(Plugin plugin);

	/**
	 *
	 * @param sender
	 * @param commandLabel
	 * @param exception
	 */
	void showCommandError(CommandSender sender, String commandLabel, Throwable exception);
}
