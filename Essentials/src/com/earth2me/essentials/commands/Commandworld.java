package com.earth2me.essentials.commands;

import static com.earth2me.essentials.I18n.tl;
import com.earth2me.essentials.Trade;
import com.earth2me.essentials.User;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;


public class Commandworld extends EssentialsCommand
{
	public Commandworld()
	{
		super("world");
	}

	@Override
	protected void run(final Server server, final User user, final String commandLabel, final String[] args) throws Exception
	{
		World world;

		if (args.length < 1)
		{
			World nether = ess.getWorld(user.getWorld().getName() + "_nether");

            final List<World> worlds = server.getWorlds();

            if (nether == null)
            {


                for (World world2 : worlds)
                {
                    if (world2.getEnvironment() == World.Environment.NETHER)
                    {
                        nether = world2;
                        break;
                    }
                }
            }
            if (nether == null) {
                throw new NotEnoughArgumentsException();
            }
            world = user.getWorld() == nether ? worlds.get(0) : nether;
		}
		else
		{
			world = ess.getWorld(condenseArgs(args));
			if (world == null)
			{
				user.sendMessage(tl("invalidWorld"));
				user.sendMessage(tl("possibleWorlds", server.getWorlds().size() - 1));
				user.sendMessage(tl("typeWorldName"));
				throw new NoChargeException();
			}
		}

		if (ess.getSettings().isWorldTeleportPermissions() && !user.isAuthorized("essentials.worlds." + world.getName()))
		{
			throw new Exception(tl("noPerm", "essentials.worlds." + world.getName()));
		}

        Location target;

        if (args.length > 1 && args[args.length - 1].equals("spawn"))
        {
            target = world.getSpawnLocation();
        }
        else
        {
            double factor;
            if (user.getWorld().getEnvironment() == World.Environment.NETHER && world.getEnvironment() == World.Environment.NORMAL)
            {
                factor = 8.0;
            } else if (user.getWorld().getEnvironment() == World.Environment.NORMAL && world.getEnvironment() == World.Environment.NETHER)
            {
                factor = 1.0 / 8.0;
            } else
            {
                factor = 1.0;
            }

            Location loc = user.getLocation();
            target = new Location(world, loc.getBlockX() * factor + .5, loc.getBlockY(), loc.getBlockZ() * factor + .5);

        }
        final Trade charge = new Trade(this.getName(), ess);
        charge.isAffordableFor(user);
        user.getTeleport().teleport(target, charge, TeleportCause.COMMAND);
        throw new NoChargeException();
	}

    String condenseArgs(String[] args)
    {
        if (args.length == 1)
        {
            return args[0];
        }
        final StringBuilder bldr = new StringBuilder();
        for (int i = 0; i < args.length - 1; i++)
        {
            if (i != 0)
            {
                bldr.append(" ");
            }
            bldr.append(args[i]);
        }

        if (!args[args.length - 1].equals("spawn"))
        {
            bldr.append(" ").append(args[args.length - 1]);
        }
        return bldr.toString();
    }
}
