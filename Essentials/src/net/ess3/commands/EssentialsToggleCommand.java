package net.ess3.commands;

import java.util.Set;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import net.ess3.api.IUser;


public abstract class EssentialsToggleCommand extends EssentialsSettingsCommand
{

	protected void setValue(final IUser player, final boolean value)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	protected boolean getValue(final IUser player)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	protected boolean canMatch(final String arg)
	{
		if (arg.equalsIgnoreCase("on") || arg.startsWith("enable") || arg.equalsIgnoreCase("1"))
		{
			return true;
		}
		else if (arg.equalsIgnoreCase("off") || arg.startsWith("disable") || arg.equalsIgnoreCase("0"))
		{
			return true;
		}
		return false;
	}

	protected void playerMatch(final IUser player, final String arg)
	{
		if (arg == null)
		{
			setValue(player, !getValue(player));
		}
		else {
			if (arg.contains("on") || arg.contains("ena") || arg.equalsIgnoreCase("1"))
			{
				setValue(player, true);
			}
			else
			{
				setValue(player, false);
			}
		}
	}
}
