package com.earth2me.essentials.commands;

import static com.earth2me.essentials.I18n.tl;
import com.earth2me.essentials.User;
import org.bukkit.Server;


public class Commandcompass extends EssentialsCommand
{
	public Commandcompass()
	{
		super("compass");
	}

	@Override
	public void run(final Server server, final User user, final String commandLabel, final String[] args) throws Exception
	{
		final int bearing = (int)(user.getLocation().getYaw() + 180 + 360) % 360;
		String dir;
		if (bearing < 23)
		{
			dir = "north";
		}
		else if (bearing < 68)
		{
			dir = "northeast";
		}
		else if (bearing < 113)
		{
			dir = "east";
		}
		else if (bearing < 158)
		{
			dir = "southeast";
		}
		else if (bearing < 203)
		{
			dir = "south";
		}
		else if (bearing < 248)
		{
			dir = "southwest";
		}
		else if (bearing < 293)
		{
			dir = "west";
		}
		else if (bearing < 338)
		{
			dir = "northwest";
		}
		else
		{
			dir = "north";
		}
		user.sendMessage(tl("compassBearing", tl(dir), bearing));
	}
}
