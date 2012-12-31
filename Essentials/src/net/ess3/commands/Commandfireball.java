package net.ess3.commands;

import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.entity.SmallFireball;
import org.bukkit.util.Vector;
import net.ess3.api.IUser;


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

		final Player player = user.getPlayer();

		final Vector direction = player.getEyeLocation().getDirection().multiply(2);
		final Fireball fireball = player.getWorld().spawn(
				player.getEyeLocation().add(direction.getX(), direction.getY(), direction.getZ()), small ? SmallFireball.class : Fireball.class);
		fireball.setShooter(player);
	}
}
