package net.ess3.api;

import net.ess3.economy.Trade;
import net.ess3.utils.Target;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;


public interface ITeleport
{
	/**
	 * Teleport to a location now
	 * @param loc - location to send to
	 * @param cooldown - To check cooldown
	 * @param cause - Why was the send caused? Use a {@link TeleportCause}
	 * @throws Exception
	 */
	void now(Location loc, boolean cooldown, TeleportCause cause) throws Exception;

	void now(Entity entity, boolean cooldown, TeleportCause cause) throws Exception;
	
	void now(final Target target, final TeleportCause cause) throws Exception;	

	void back(Trade chargeFor) throws Exception;

	void teleport(Location bed, Trade charge, TeleportCause teleportCause) throws Exception;

	void teleport(Entity entity, Trade chargeFor, TeleportCause cause) throws Exception;
	
	void teleportToMe(IUser otherUser, Trade chargeFor, TeleportCause cause) throws Exception;

	void home(Location loc, Trade chargeFor) throws Exception;

	void respawn(Trade charge, TeleportCause teleportCause) throws Exception;

	void back() throws Exception;

	public void warp(String name, Trade charge, TeleportCause teleportCause) throws Exception;
}
