package com.earth2me.essentials.components.commands.handlers;

import static com.earth2me.essentials.I18n._;
import com.earth2me.essentials.Trade;
import com.earth2me.essentials.components.commands.EssentialsCommand;
import com.earth2me.essentials.components.commands.NoChargeException;
import com.earth2me.essentials.components.users.IUser;
import com.earth2me.essentials.perm.WorldPermissions;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.World;
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

			final List<World> worlds = getServer().getWorlds();

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
			world = user.getWorld() == nether ? worlds.get(0) : nether;
		}
		else
		{
			world = getContext().getWorld(getFinalArg(args, 0));
			if (world == null)
			{
				user.sendMessage(_("invalidWorld"));
				user.sendMessage(_("possibleWorlds", getServer().getWorlds().size() - 1));
				user.sendMessage(_("typeWorldName"));
				throw new NoChargeException();
			}
		}


		if (!WorldPermissions.getPermission(world.getName()).isAuthorized(user))
		{
			user.sendMessage(_("invalidWorld")); //TODO: Make a "world teleport denied" translation
			throw new NoChargeException();
		}

		double factor;
		if (user.getWorld().getEnvironment() == World.Environment.NETHER && world.getEnvironment() == World.Environment.NORMAL)
		{
			factor = 8.0;
		}
		else if (user.getWorld().getEnvironment() == World.Environment.NORMAL && world.getEnvironment() == World.Environment.NETHER)
		{
			factor = 1.0 / 8.0;
		}
		else
		{
			factor = 1.0;
		}

		final Location loc = user.getLocation();
		final Location target = new Location(world, loc.getBlockX() * factor + .5, loc.getBlockY(), loc.getBlockZ() * factor + .5);

		final Trade charge = new Trade(getCommandName(), getContext());
		charge.isAffordableFor(user);
		user.getTeleport().teleport(target, charge, TeleportCause.COMMAND);
		throw new NoChargeException();
	}
}
