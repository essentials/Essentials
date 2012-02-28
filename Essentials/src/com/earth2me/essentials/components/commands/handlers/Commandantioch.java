package com.earth2me.essentials.components.commands.handlers;

import com.earth2me.essentials.Util;
import com.earth2me.essentials.components.commands.EssentialsCommand;
import com.earth2me.essentials.components.settings.users.IUserComponent;
import org.bukkit.Location;
import org.bukkit.entity.TNTPrimed;


public class Commandantioch extends EssentialsCommand
{
	@Override
	public void run(final IUserComponent user, final String commandLabel, final String[] args) throws Exception
	{
		getContext().getMessager().broadcastMessage(user, "...lobbest thou thy Holy Hand Grenade of Antioch towards thy foe,");
		getContext().getMessager().broadcastMessage(user, "who being naughty in My sight, shall snuff it.");

		final Location loc = Util.getTarget(user);
		loc.getWorld().spawn(loc, TNTPrimed.class);
	}
}
