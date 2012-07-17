package net.ess3.commands;

import net.ess3.api.IUser;
//TODO: remove bukkit
import org.bukkit.entity.Fireball;
import org.bukkit.entity.SmallFireball;
import org.bukkit.util.Vector;


public class Commandfireball extends EssentialsCommand
{
	@Override
	protected void run(final IUser user, final String commandLabel, final String[] args) throws Exception
	{
		boolean small = false;
		if (args.length > 0 && args[0].equalsIgnoreCase("small"))
		{
			small = true;
		}
		final Vector direction = user.getEyeLocation().getDirection().multiply(2);
		final Fireball fireball = user.getWorld().spawn(user.getEyeLocation().add(direction.getX(), direction.getY(), direction.getZ()), small ? SmallFireball.class : Fireball.class);
		fireball.setShooter(user.getBase());
	}
}
