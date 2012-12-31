package net.ess3.commands;

import static net.ess3.I18n._;
import java.util.Locale;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import net.ess3.api.IUser;


//Todo: Fix this up
public class Commandremove extends EssentialsCommand
{
	private enum ToRemove
	{
		DROPS,
		ARROWS,
		BOATS,
		MINECARTS,
		XP,
		PAINTINGS
	}

	@Override
	protected void run(final IUser user, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 1)
		{
			throw new NotEnoughArgumentsException();
		}
		final ToRemove toRemove;
		final World world = user.getPlayer().getWorld();
		int radius = 0;

		if (args.length < 2)
		{
			try
			{
				radius = Integer.parseInt(args[1]);
			}
			catch (NumberFormatException e)
			{
				throw new Exception(_("numberRequired"), e);
			}
		}

		try
		{
			toRemove = ToRemove.valueOf(args[0].toUpperCase(Locale.ENGLISH));
		}
		catch (IllegalArgumentException e)
		{
			throw new NotEnoughArgumentsException(e); //TODO: translate and list types
		}

		removeEntities(user, world, toRemove, radius);
	}

	@Override
	protected void run(final CommandSender sender, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 2)
		{
			throw new NotEnoughArgumentsException();
		}
		final World world = ess.getWorld(args[1]);

		if (world == null)
		{
			throw new Exception(_("invalidWorld"));
		}
		final ToRemove toRemove;
		try
		{
			toRemove = ToRemove.valueOf(args[0].toUpperCase(Locale.ENGLISH));
		}
		catch (IllegalArgumentException e)
		{
			throw new NotEnoughArgumentsException(e); //TODO: translate and list types
		}
		removeEntities(sender, world, toRemove, 0);
	}

	protected void removeEntities(final CommandSender sender, final World world, final ToRemove toRemove, int radius) throws Exception
	{
		int removed = 0;
		if (radius > 0)
		{
			radius *= radius;
		}

		for (Chunk chunk : world.getLoadedChunks())
		{
			for (Entity e : chunk.getEntities())
			{
				if (radius > 0)
				{
					if (((Player)sender).getLocation().distanceSquared(e.getLocation()) > radius)
					{
						continue;
					}
				}
				switch (toRemove)
				{
				case DROPS:
					if (e instanceof Item)
					{
						e.remove();
						removed++;
					}
					break;
				case ARROWS:
					if (e instanceof Projectile)
					{
						e.remove();
						removed++;
					}
					break;
				case BOATS:
					if (e instanceof Boat)
					{
						e.remove();
						removed++;
					}
					break;
				case MINECARTS:
					if (e instanceof Minecart)
					{
						e.remove();
						removed++;
					}
					break;
				case PAINTINGS:
					if (e instanceof Painting)
					{
						e.remove();
						removed++;
					}
					break;
				case XP:
					if (e instanceof ExperienceOrb)
					{
						e.remove();
						removed++;
					}
					break;
				}

			}
		}
		sender.sendMessage(_("removed", removed));
	}
}
