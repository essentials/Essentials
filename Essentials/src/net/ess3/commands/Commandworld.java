package net.ess3.commands;

import java.util.List;
import static net.ess3.I18n._;
import net.ess3.api.IUser;
import net.ess3.economy.Trade;
import net.ess3.permissions.Permissions;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;


public class Commandworld extends EssentialsCommand
{
	@Override
	protected void run(final IUser user, final String commandLabel, final String[] args) throws Exception
	{
		World world;

		if (args.length < 1)
		{
			World nether = null;

			final List<World> worlds = server.getWorlds();

			for (World world2 : worlds)
			{
				if (world2.getEnvironment() == World.Environment.NETHER)
				{
					nether = world2;
					break;
				}
			}
			if (nether == null)
			{
				return;
			}
			world = user.getPlayer().getWorld() == nether ? worlds.get(0) : nether;
		}
		else
		{
			world = ess.getWorld(getFinalArg(args, 0));
			if (world == null)
			{
				user.sendMessage(_("invalidWorld"));
				user.sendMessage(_("possibleWorlds", server.getWorlds().size() - 1));
				user.sendMessage(_("typeWorldName"));
				throw new NoChargeException();
			}
		}


		if (!Permissions.WORLD.isAuthorized(user, world.getName()))
		{
			user.sendMessage(_("deniedWorldAccess", world.getName()));
			throw new NoChargeException();
		}

		final double factor;
		final Player player = user.getPlayer();
		if (player.getWorld().getEnvironment() == World.Environment.NETHER && world.getEnvironment() == World.Environment.NORMAL)
		{
			factor = 8d;
		}
		else if (player.getWorld().getEnvironment() == World.Environment.NORMAL && world.getEnvironment() == World.Environment.NETHER)
		{
			factor = 1d / 8d;
		}
		else
		{
			factor = 1d;
		}

		final Location loc = player.getLocation();
		final Location target = new Location(world, loc.getBlockX() * factor + .5, loc.getBlockY(), loc.getBlockZ() * factor + .5);

		final Trade charge = new Trade(commandName, ess);
		charge.isAffordableFor(user);
		user.getTeleport().teleport(target, charge, TeleportCause.COMMAND);
		throw new NoChargeException();
	}
}
