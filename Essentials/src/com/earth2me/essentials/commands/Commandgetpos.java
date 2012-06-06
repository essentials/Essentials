package com.earth2me.essentials.commands;

import com.earth2me.essentials.api.IUser;
import com.earth2me.essentials.permissions.Permissions;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;


public class Commandgetpos extends EssentialsCommand
{
	@Override
	public void run(final IUser user, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length > 0 && Permissions.GETPOS_OTHERS.isAuthorized(user))
		{
			//todo permissions
			final IUser otherUser = getPlayer(args, 0);
			if (!otherUser.isHidden() || Permissions.LIST_HIDDEN.isAuthorized(user))
			{
				outputPosition(user, otherUser.getLocation(), user.getLocation());
				return;
			}

		}
		outputPosition(user, user.getLocation(), null);
	}

	@Override
	protected void run(final CommandSender sender, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 1)
		{
			throw new NotEnoughArgumentsException();
		}
		final IUser user = getPlayer(args, 0);
		outputPosition(sender, user.getLocation(), null);
	}

	//TODO: Translate
	private void outputPosition(final CommandSender sender, final Location coords, final Location distance)
	{
		sender.sendMessage("§7World: " + coords.getWorld().getName());
		sender.sendMessage("§7X: " + coords.getBlockX() + " (+East <-> -West)");
		sender.sendMessage("§7Y: " + coords.getBlockY() + " (+Up <-> -Down)");
		sender.sendMessage("§7Z: " + coords.getBlockZ() + " (+South <-> -North)");
		sender.sendMessage("§7Yaw: " + (coords.getYaw() + 180 + 360) % 360 + " (Rotation)");
		sender.sendMessage("§7Pitch: " + coords.getPitch() + " (Head angle)");
		if (distance != null && coords.getWorld().equals(distance.getWorld()))
		{
			sender.sendMessage("§7Distance: " + coords.distance(distance));
		}
	}
}
