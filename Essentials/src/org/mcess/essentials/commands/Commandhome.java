package org.mcess.essentials.commands;

import org.mcess.essentials.Trade;
import org.mcess.essentials.User;
import org.mcess.essentials.utils.StringUtil;
import java.util.List;
import java.util.Locale;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.mcess.essentials.I18n;


public class Commandhome extends EssentialsCommand
{
	public Commandhome()
	{
		super("home");
	}

	// This method contains an undocumented translation parameters #EasterEgg
	@Override
	public void run(final Server server, final User user, final String commandLabel, final String[] args) throws Exception
	{
		final Trade charge = new Trade(this.getName(), ess);
		User player = user;
		String homeName = "";
		String[] nameParts;
		if (args.length > 0)
		{
			nameParts = args[0].split(":");
			if (nameParts[0].length() == args[0].length() || !user.isAuthorized("essentials.home.others"))
			{
				homeName = nameParts[0];
			}
			else
			{
				player = getPlayer(server, nameParts, 0, true, true);
				if (nameParts.length > 1)
				{
					homeName = nameParts[1];
				}
			}
		}
		try
		{
			if ("bed".equalsIgnoreCase(homeName) && user.isAuthorized("essentials.home.bed"))
			{
				final Location bed = player.getBase().getBedSpawnLocation();
				if (bed != null)
				{
					user.getTeleport().teleport(bed, charge, TeleportCause.COMMAND);
					throw new NoChargeException();
				}
				else
				{
					throw new Exception(I18n.tl("bedMissing"));
				}
			}
			goHome(user, player, homeName.toLowerCase(Locale.ENGLISH), charge);
		}
		catch (NotEnoughArgumentsException e)
		{
			Location bed = player.getBase().getBedSpawnLocation();
			final List<String> homes = player.getHomes();
			if (homes.isEmpty() && player.equals(user))
			{
				user.getTeleport().respawn(charge, TeleportCause.COMMAND);
			}
			else if (homes.isEmpty())
			{
				throw new Exception(I18n.tl("noHomeSetPlayer"));
			}
			else if (homes.size() == 1 && player.equals(user))
			{
				goHome(user, player, homes.get(0), charge);
			}
			else
			{
				final int count = homes.size();
				if (user.isAuthorized("essentials.home.bed"))
				{
					if (bed != null)
					{
						homes.add(I18n.tl("bed"));
					}
					else
					{
						homes.add(I18n.tl("bedNull"));
					}
				}
				user.sendMessage(I18n.tl("homes", StringUtil.joinList(homes), count, getHomeLimit(player)));
			}
		}
		throw new NoChargeException();
	}

	private String getHomeLimit(final User player)
	{
		if (!player.getBase().isOnline())
		{
			return "?";
		}
		if (player.isAuthorized("essentials.sethome.multiple.unlimited"))
		{
			return "*";
		}
		return Integer.toString(ess.getSettings().getHomeLimit(player));
	}

	private void goHome(final User user, final User player, final String home, final Trade charge) throws Exception
	{		
		if (home.length() < 1)
		{
			throw new NotEnoughArgumentsException();
		}
		final Location loc = player.getHome(home);
		if (loc == null)
		{
			throw new NotEnoughArgumentsException();
		}
		if (user.getWorld() != loc.getWorld() && ess.getSettings().isWorldHomePermissions()
			&& !user.isAuthorized("essentials.worlds." + loc.getWorld().getName()))
		{
			throw new Exception(I18n.tl("noPerm", "essentials.worlds." + loc.getWorld().getName()));
		}
		user.getTeleport().teleport(loc, charge, TeleportCause.COMMAND);
	}
}
