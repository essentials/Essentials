package net.ess3.commands;

import static net.ess3.I18n._;
import net.ess3.api.IUser;
import net.ess3.economy.Trade;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;


public class Commandtop extends EssentialsCommand
{
	@Override
	public void run(final IUser user, final String commandLabel, final String[] args) throws Exception
	{
		final Player player = user.getPlayer();
		final Location playerLocation = user.getPlayer().getLocation();
		final int topX = playerLocation.getBlockX();
		final int topZ = playerLocation.getBlockZ();
		final int topY = playerLocation.getWorld().getHighestBlockYAt(topX, topZ);
		user.getTeleport().teleport(new Location(player.getWorld(), topX, topY + 1, topZ, playerLocation.getYaw(), playerLocation.getPitch()), new Trade(commandName, ess), TeleportCause.COMMAND);
		user.sendMessage(_("teleportTop"));
	}
}
