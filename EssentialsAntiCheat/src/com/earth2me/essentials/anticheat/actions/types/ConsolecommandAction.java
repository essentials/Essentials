package com.earth2me.essentials.anticheat.actions.types;

import com.earth2me.essentials.anticheat.NoCheatPlayer;
import com.earth2me.essentials.anticheat.checks.Check;


/**
 * Execute a command by imitating an admin typing the command directly into the console
 *
 */
public class ConsolecommandAction extends ActionWithParameters
{
	public ConsolecommandAction(String name, int delay, int repeat, String command)
	{
		// Log messages may have color codes now
		super(name, delay, repeat, command);
	}

	/**
	 * Fill in the placeholders ( stuff that looks like '[something]') with information, make a nice String out of it
	 * that can be directly used as a command in the console.
	 *
	 * @param player The player that is used to fill in missing data
	 * @param check The check that is used to fill in missing data
	 * @return The complete, ready to use, command
	 */
	public String getCommand(NoCheatPlayer player, Check check)
	{
		return super.getMessage(player, check);
	}

	/**
	 * Convert the commands data into a string that can be used in the config files
	 */
	public String toString()
	{
		return "cmd:" + name + ":" + delay + ":" + repeat;
	}
}
