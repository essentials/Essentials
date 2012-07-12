package net.ess3.commands;

import java.util.List;
import java.util.Locale;
import static net.ess3.I18n._;
import net.ess3.api.IUser;
import net.ess3.economy.Trade;
import net.ess3.permissions.Permissions;
import net.ess3.permissions.WorldPermissions;
import net.ess3.utils.Util;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;


public class Commandhome extends EssentialsCommand
{
	@Override
	public void run(final IUser user, final String commandLabel, final String[] args) throws Exception
	{
		final Trade charge = new Trade(commandName, ess);
		charge.isAffordableFor(user);
		IUser player = user;
		String homeName = "";
		String[] nameParts;
		if (args.length > 0)
		{
			nameParts = args[0].split(":");
			if (nameParts[0].length() == args[0].length() || !Permissions.HOME_OTHERS.isAuthorized(user))
			{
				homeName = nameParts[0];
			}
			else
			{
				player = getPlayer(nameParts, 0, true);
				if (nameParts.length > 1)
				{
					homeName = nameParts[1];
				}
			}
		}
		try
		{
			if ("bed".equalsIgnoreCase(homeName))
			{
				final Location bed = player.getBedSpawnLocation();
				if (bed != null && bed.getBlock().getType() == Material.BED_BLOCK)
				{
					user.getTeleport().teleport(bed, charge, TeleportCause.COMMAND);
					throw new NoChargeException();
				}
			}
			goHome(user, player, homeName.toLowerCase(Locale.ENGLISH), charge);
		}
		catch (NotEnoughArgumentsException e)
		{
			Location bed = player.getBedSpawnLocation();
			if (bed != null && bed.getBlock().getType() != Material.BED_BLOCK)
			{
				bed = null;
			}
			final List<String> homes = player.getHomes();
			if (homes.isEmpty() && player.equals(user))
			{
				if (bed != null)
				{
					user.getTeleport().teleport(bed, charge, TeleportCause.COMMAND);
					throw new NoChargeException();
				}
				user.getTeleport().respawn(charge, TeleportCause.COMMAND);

			}
			else if (homes.isEmpty())
			{
				throw new Exception(player == user ? _("noHomeSet") : _("noHomeSetPlayer"));
			}
			else if (homes.size() == 1 && player.equals(user))
			{
				goHome(user, player, homes.get(0), charge);
			}
			else
			{
				if (bed != null)
				{
					homes.add("bed");
				}
				user.sendMessage(_("homes", Util.joinList(homes)));
			}
		}
		throw new NoChargeException();
	}

	private void goHome(final IUser user, final IUser player, final String home, final Trade charge) throws Exception
	{
		final Location loc = player.getHome(home);
		if (loc == null)
		{
			throw new NotEnoughArgumentsException();
		}
		if (user.getWorld() != loc.getWorld() && ess.getSettings().getData().getGeneral().isWorldHomePermissions()
			&& !WorldPermissions.getPermission(loc.getWorld().getName()).isAuthorized(user))
		{
			throw new Exception(_("noPerm", "essentials.world." + loc.getWorld().getName()));
		}
		user.getTeleport().home(loc, charge);
	}
}
