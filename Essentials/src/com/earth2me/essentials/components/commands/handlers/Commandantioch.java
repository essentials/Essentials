package com.earth2me.essentials.components.commands.handlers;

import com.earth2me.essentials.Util;
import com.earth2me.essentials.components.commands.EssentialsCommand;
import com.earth2me.essentials.components.users.IUser;
import org.bukkit.Location;
import org.bukkit.entity.TNTPrimed;


public class Commandantioch extends EssentialsCommand
{
	@Override
	public void run(final IUser user, final String commandLabel, final String[] args) throws Exception
	{
		getContext().broadcastMessage(user, "...lobbest thou thy Holy Hand Grenade of Antioch towards thy foe,");
		getContext().broadcastMessage(user, "who being naughty in My sight, shall snuff it.");

		final Location loc = Util.getTarget(user);
		loc.getWorld().spawn(loc, TNTPrimed.class);
	}
}
