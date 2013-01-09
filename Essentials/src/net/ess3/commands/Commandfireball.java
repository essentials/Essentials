package net.ess3.commands;

import org.bukkit.entity.*;
import org.bukkit.util.Vector;
import net.ess3.api.IUser;


public class Commandfireball extends EssentialsCommand
{
	@Override
	protected void run(final IUser user, final String commandLabel, final String[] args) throws Exception
	{
		Class<? extends Entity> clazz = Fireball.class;
		if (args.length > 0)
		{
			if(args[0].equalsIgnoreCase("small"))
			{
				clazz = SmallFireball.class;
			}
			else if(args[0].equalsIgnoreCase("arrow"))
			{
				clazz = Arrow.class;
			}
			else if(args[0].equalsIgnoreCase("skull"))
			{
				clazz = WitherSkull.class;
			}
			else if(args[0].equalsIgnoreCase("egg"))
			{
				clazz = Egg.class;
			}
			else if(args[0].equalsIgnoreCase("snowball"))
			{
				clazz = Snowball.class;
			}
			else if(args[0].equalsIgnoreCase("expbottle"))
			{
				clazz = ThrownExpBottle.class;
			}
			else if(args[0].equalsIgnoreCase("large"))
			{
				clazz = LargeFireball.class;
			}
		}

		final Player player = user.getPlayer();

		final Vector direction = player.getEyeLocation().getDirection().multiply(2);
		Projectile projectile = (Projectile)user.getPlayer().getWorld().spawn(user.getPlayer().getEyeLocation().add(direction.getX(), direction.getY(), direction.getZ()), clazz);
		projectile.setShooter(user.getPlayer());
		projectile.setVelocity(direction);
	}
}
