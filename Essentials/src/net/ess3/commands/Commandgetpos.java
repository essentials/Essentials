package net.ess3.commands;

import net.ess3.api.IUser;
import net.ess3.permissions.Permissions;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import static net.ess3.I18n._;



public class Commandgetpos extends EssentialsCommand
{
	@Override
	public void run(final IUser user, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length > 0 && Permissions.GETPOS_OTHERS.isAuthorized(user))
		{
			//todo permissions
			final IUser otherUser = ess.getUserMap().matchUser(args[0], false);
			if (user.getPlayer().canSee(otherUser.getPlayer()) || Permissions.LIST_HIDDEN.isAuthorized(user))
			{
				outputPosition(user, otherUser.getPlayer().getLocation(), user.getPlayer().getLocation());
				return;
			}

		}
		outputPosition(user, user.getPlayer().getLocation(), null);
	}

	@Override
	protected void run(final CommandSender sender, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 1)
		{
			throw new NotEnoughArgumentsException();
		}
		final IUser user = ess.getUserMap().matchUser(args[0], false);
		outputPosition(sender, user.getPlayer().getLocation(), null);
	}

	private void outputPosition(final CommandSender sender, final Location coords, final Location distance)
	{
		sender.sendMessage(_("getposWorld", coords.getWorld().getName()));
		sender.sendMessage(_("getposX", coords.getBlockX()));
		sender.sendMessage(_("getposY", coords.getBlockY()));
		sender.sendMessage(_("getposZ", coords.getBlockZ()));
		sender.sendMessage(_("getposYaw", (coords.getYaw() + 540) % 360));
		sender.sendMessage(_("getposPitch", coords.getPitch()));
		if (distance != null && coords.getWorld().equals(distance.getWorld()))
		{
			sender.sendMessage(_("getposDistance", coords.distance(distance)));
		}
	}
}
