package com.earth2me.essentials.anticheat.actions.types;

import com.earth2me.essentials.anticheat.NoCheatPlayer;
import com.earth2me.essentials.anticheat.checks.Check;


/**
 * Print a log message to various locations
 *
 */
public class LogAction extends ActionWithParameters
{
	// Some flags to decide where the log message should show up, based on
	// the config file
	private final boolean toChat;
	private final boolean toConsole;
	private final boolean toFile;

	public LogAction(String name, int delay, int repeat, boolean toChat, boolean toConsole, boolean toFile, String message)
	{
		super(name, delay, repeat, message);
		this.toChat = toChat;
		this.toConsole = toConsole;
		this.toFile = toFile;
	}

	/**
	 * Parse the final log message out of various data from the player and check that triggered the action.
	 *
	 * @param player The player that is used as a source for the log message
	 * @param check The check that is used as a source for the log message
	 * @return
	 */
	public String getLogMessage(NoCheatPlayer player, Check check)
	{
		return super.getMessage(player, check);
	}

	/**
	 * Should the message be shown in chat?
	 *
	 * @return true, if yes
	 */
	public boolean toChat()
	{
		return toChat;
	}

	/**
	 * Should the message be shown in the console?
	 *
	 * @return true, if yes
	 */
	public boolean toConsole()
	{
		return toConsole;
	}

	/**
	 * Should the message be written to the logfile?
	 *
	 * @return true, if yes
	 */
	public boolean toFile()
	{
		return toFile;
	}

	/**
	 * Create the string that's used to define the action in the logfile
	 */
	public String toString()
	{
		return "log:" + name + ":" + delay + ":" + repeat + ":" + (toConsole ? "c" : "") + (toChat ? "i" : "") + (toFile ? "f" : "");
	}
}
