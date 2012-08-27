package net.ess3.commands;

import static net.ess3.I18n._;
import net.ess3.api.IUser;
import net.ess3.economy.Trade;
import org.bukkit.Location;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;


public class Commandtop extends EssentialsCommand
{
	@Override
	public void run(final IUser user, final String commandLabel, final String[] args) throws Exception
	{
		final int topX = user.getPlayer().getLocation().getBlockX();
		final int topZ = user.getPlayer().getLocation().getBlockZ();
		final int topY = user.getPlayer().getWorld().getHighestBlockYAt(topX, topZ);
		user.getTeleport().teleport(new Location(user.getPlayer().getWorld(), user.getPlayer().getLocation().getX(), topY + 1, user.getPlayer().getLocation().getZ(), user.getPlayer().getLocation().getYaw(), user.getPlayer().getLocation().getPitch()), new Trade(commandName, ess), TeleportCause.COMMAND);
		user.sendMessage(_("teleportTop"));
	}
}
