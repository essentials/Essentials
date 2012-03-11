package com.earth2me.essentials.anticheat.actions.types;

import com.earth2me.essentials.anticheat.NoCheatPlayer;
import com.earth2me.essentials.anticheat.actions.Action;
import com.earth2me.essentials.anticheat.actions.ParameterName;
import com.earth2me.essentials.anticheat.checks.Check;
import java.util.ArrayList;


/**
 * Action with parameters is used to
 *
 */
public abstract class ActionWithParameters extends Action
{
	private final ArrayList<Object> messageParts;

	public ActionWithParameters(String name, int delay, int repeat, String message)
	{
		super(name, delay, repeat);

		messageParts = new ArrayList<Object>();

		parseMessage(message);
	}

	private void parseMessage(String message)
	{
		String parts[] = message.split("\\[", 2);

		// No opening braces left
		if (parts.length != 2)
		{
			messageParts.add(message);
		}
		// Found an opening brace
		else
		{
			String parts2[] = parts[1].split("\\]", 2);

			// Found no matching closing brace
			if (parts2.length != 2)
			{
				messageParts.add(message);
			}
			// Found a matching closing brace
			else
			{
				ParameterName w = ParameterName.get(parts2[0]);

				if (w != null)
				{
					// Found an existing wildcard inbetween the braces
					messageParts.add(parts[0]);
					messageParts.add(w);

					// Go further down recursive
					parseMessage(parts2[1]);
				}
				else
				{
					messageParts.add(message);
				}
			}
		}
	}

	/**
	 * Get a string with all the wildcards replaced with data from LogData
	 *
	 * @param data
	 * @return
	 */
	protected String getMessage(NoCheatPlayer player, Check check)
	{

		StringBuilder log = new StringBuilder(100); // Should be big enough most
		// of the time

		for (Object part : messageParts)
		{
			if (part instanceof String)
			{
				log.append((String)part);
			}
			else
			{
				log.append(check.getParameter((ParameterName)part, player));
			}
		}

		return log.toString();
	}
}
