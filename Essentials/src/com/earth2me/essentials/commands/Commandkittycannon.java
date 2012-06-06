package com.earth2me.essentials.commands;


import com.earth2me.essentials.api.IUser;
import com.earth2me.essentials.bukkit.Mob;
import java.util.Random;
import org.bukkit.Location;
import org.bukkit.entity.Ocelot;


public class Commandkittycannon extends EssentialsCommand
{
	private static Random random = new Random();

	@Override
	protected void run(final IUser user, final String commandLabel, final String[] args) throws Exception
	{
		final Mob cat = Mob.OCELOT;
		final Ocelot ocelot = (Ocelot)cat.spawn(user, server, user.getEyeLocation());
		if (ocelot == null)
		{
			return;
		}
		final int i = random.nextInt(Ocelot.Type.values().length);
		ocelot.setCatType(Ocelot.Type.values()[i]);
		ocelot.setTamed(true);
		ocelot.setVelocity(user.getEyeLocation().getDirection().multiply(2));
		ess.scheduleSyncDelayedTask(new Runnable()
		{
			@Override
			public void run()
			{
				final Location loc = ocelot.getLocation();
				ocelot.remove();
				loc.getWorld().createExplosion(loc, 0F);
			}
		}, 20);
	}
}