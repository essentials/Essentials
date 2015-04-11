package org.mcess.essentials.commands;

import org.mcess.essentials.Trade;
import org.mcess.essentials.User;
import org.mcess.essentials.utils.LocationUtil;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.mcess.essentials.I18n;


public class Commandtop extends EssentialsCommand
{
	public Commandtop()
	{
		super("top");
	}

	@Override
	public void run(final Server server, final User user, final String commandLabel, final String[] args) throws Exception
	{
		final int topX = user.getLocation().getBlockX();
		final int topZ = user.getLocation().getBlockZ();
		final float pitch = user.getLocation().getPitch();
		final float yaw = user.getLocation().getYaw();
		final Location loc = LocationUtil.getSafeDestination(new Location(user.getWorld(), topX, user.getWorld().getMaxHeight(), topZ, yaw, pitch));
		user.getTeleport().teleport(loc, new Trade(this.getName(), ess), TeleportCause.COMMAND);
		user.sendMessage(I18n.tl("teleportTop", loc.getWorld().getName(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()));
		
	}
}
