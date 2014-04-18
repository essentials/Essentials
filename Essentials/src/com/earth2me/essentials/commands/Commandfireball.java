package com.earth2me.essentials.commands;

import com.earth2me.essentials.User;
import org.bukkit.Server;
import org.bukkit.entity.*;
import org.bukkit.util.Vector;


public class Commandfireball extends EssentialsCommand
{
	public Commandfireball()
	{
		super("fireball");
	}

	@Override
	protected void run(final Server server, final User user, final String commandLabel, final String[] args) throws Exception
	{
		Class<? extends Entity> type = Fireball.class;
		Projectile projectile;
		int speed = 2;
		if (args.length > 0)
		{
			if (args[0].equalsIgnoreCase("small"))
			{
				type = SmallFireball.class;
			}
			else if (args[0].equalsIgnoreCase("arrow"))
			{
				type = Arrow.class;
			}
			else if (args[0].equalsIgnoreCase("skull"))
			{
				type = WitherSkull.class;
			}
			else if (args[0].equalsIgnoreCase("egg"))
			{
				type = Egg.class;
			}
			else if(args[0].equalsIgnoreCase("snowball"))
			{
				type = Snowball.class;
			}
			else if(args[0].equalsIgnoreCase("expbottle"))
			{
				type = ThrownExpBottle.class;
			}
			else if(args[0].equalsIgnoreCase("large"))
			{
				type = LargeFireball.class;
			}
		}
		final Vector direction = user.getBase().getEyeLocation().getDirection().multiply(speed);
		projectile = (Projectile)user.getWorld().spawn(user.getBase().getEyeLocation().add(direction.getX(), direction.getY(), direction.getZ()), type);
		projectile.setShooter(user.getBase());
		projectile.setVelocity(direction);
	}
}
