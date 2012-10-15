package net.ess3.commands;

import static net.ess3.I18n._;
import net.ess3.api.IUser;
import net.ess3.permissions.Permissions;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class Commandnear extends EssentialsCommand
{
	@Override
	protected void run(final IUser user, final String commandLabel, final String[] args) throws Exception
	{
		long radius = ess.getSettings().getData().getCommands().getNear().getDefaultRadius();

		IUser otherUser = null;

		if (args.length > 0)
		{
			try
			{
				otherUser = ess.getUserMap().matchUserExcludingHidden(args[0], user.getPlayer());
			}
			catch (Exception ex)
			{
				try
				{
					radius = Long.parseLong(args[0]);
				}
				catch (NumberFormatException e)
				{
				}
			}
			if (args.length > 1 && otherUser != null)
			{
				try
				{
					radius = Long.parseLong(args[1]);
				}
				catch (NumberFormatException e)
				{
				}
			}
		}
		if (otherUser == null || Permissions.NEAR_OTHERS.isAuthorized(user))
		{
			user.sendMessage(_("nearbyPlayers", getLocal(otherUser == null ? user : otherUser, radius)));
		}
		else
		{
			user.sendMessage(_("noAccessCommand"));
		}
	}

	@Override
	protected void run(final CommandSender sender, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length == 0)
		{
			throw new NotEnoughArgumentsException();
		}
		final IUser otherUser = ess.getUserMap().matchUser(args[0], false);
		long radius = ess.getSettings().getData().getCommands().getNear().getDefaultRadius();
		if (args.length > 1)
		{
			try
			{
				radius = Long.parseLong(args[1]);
			}
			catch (NumberFormatException e)
			{
			}
		}
		sender.sendMessage(_("nearbyPlayers", getLocal(otherUser, radius)));
	}

	private String getLocal(final IUser user, final long radius)
	{
		final Location loc = user.getPlayer().getLocation();
		final World world = loc.getWorld();
		final StringBuilder output = new StringBuilder();
		final long radiusSquared = radius * radius;
		Player userPlayer = user.getPlayer();

		for (Player onlinePlayer : server.getOnlinePlayers())
		{
			if (!onlinePlayer.getName().equals(user.getName()) && userPlayer.canSee(onlinePlayer))
			{
				final Location playerLoc = onlinePlayer.getLocation();
				if (playerLoc.getWorld() != world)
				{
					continue;
				}

				final long delta = (long)playerLoc.distanceSquared(loc);
				if (delta < radiusSquared)
				{
					if (output.length() > 0)
					{
						output.append(", ");
					}
					output.append(onlinePlayer.getDisplayName()).append("§f(§4").append((long)Math.sqrt(delta)).append("m§f)");
				}
			}
		}
		return output.length() > 1 ? output.toString() : _("none");
	}
}
