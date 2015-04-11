package org.mcess.essentials.commands;

import org.mcess.essentials.CommandSource;
import org.mcess.essentials.User;
import org.bukkit.Location;
import org.bukkit.Server;
import org.mcess.essentials.I18n;


public class Commandgetpos extends EssentialsCommand
{
	public Commandgetpos()
	{
		super("getpos");
	}

	@Override
	public void run(final Server server, final User user, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length > 0 && user.isAuthorized("essentials.getpos.others"))
		{
			final User otherUser = getPlayer(server, user, args, 0);
			outputPosition(user.getSource(), otherUser.getLocation(), user.getLocation());
			return;
		}
		outputPosition(user.getSource(), user.getLocation(), null);
	}

	@Override
	protected void run(final Server server, final CommandSource sender, final String commandLabel, final String[] args) throws Exception
	{
		if (args.length < 1)
		{
			throw new NotEnoughArgumentsException();
		}
		final User user = getPlayer(server, args, 0, true, false);
		outputPosition(sender, user.getLocation(), null);
	}

	private void outputPosition(final CommandSource sender, final Location coords, final Location distance)
	{
		sender.sendMessage(I18n.tl("currentWorld", coords.getWorld().getName()));
		sender.sendMessage(I18n.tl("posX", coords.getBlockX()));
		sender.sendMessage(I18n.tl("posY", coords.getBlockY()));
		sender.sendMessage(I18n.tl("posZ", coords.getBlockZ()));
		sender.sendMessage(I18n.tl("posYaw", (coords.getYaw() + 180 + 360) % 360));
		sender.sendMessage(I18n.tl("posPitch", coords.getPitch()));
		if (distance != null && coords.getWorld().equals(distance.getWorld()))
		{
			sender.sendMessage(I18n.tl("distance", coords.distance(distance)));
		}
	}
}
