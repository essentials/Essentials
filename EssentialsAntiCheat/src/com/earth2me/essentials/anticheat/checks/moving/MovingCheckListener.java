package com.earth2me.essentials.anticheat.checks.moving;

import com.earth2me.essentials.anticheat.EventManager;
import com.earth2me.essentials.anticheat.NoCheat;
import com.earth2me.essentials.anticheat.NoCheatPlayer;
import com.earth2me.essentials.anticheat.checks.CheckUtil;
import com.earth2me.essentials.anticheat.config.ConfigurationCacheStore;
import com.earth2me.essentials.anticheat.config.Permissions;
import com.earth2me.essentials.anticheat.data.PreciseLocation;
import java.util.LinkedList;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.*;
import org.bukkit.util.Vector;


/**
 * Central location to listen to events that are relevant for the moving checks
 *
 */
public class MovingCheckListener implements Listener, EventManager
{
	private final MorePacketsCheck morePacketsCheck;
	private final FlyingCheck flyingCheck;
	private final RunningCheck runningCheck;
	private final NoCheat plugin;

	public MovingCheckListener(NoCheat plugin)
	{

		flyingCheck = new FlyingCheck(plugin);
		runningCheck = new RunningCheck(plugin);
		morePacketsCheck = new MorePacketsCheck(plugin);

		this.plugin = plugin;
	}

	/**
	 * A workaround for players placing blocks below them getting pushed off the block by NoCheat.
	 *
	 * It essentially moves the "setbackpoint" to the top of the newly placed block, therefore tricking NoCheat into
	 * thinking the player was already on top of that block and should be allowed to stay there
	 *
	 * @param event The BlockPlaceEvent
	 */
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void blockPlace(final BlockPlaceEvent event)
	{
		final NoCheatPlayer player = plugin.getPlayer(event.getPlayer());
		final MovingConfig config = MovingCheck.getConfig(player);

		// If the player is allowed to fly anyway, the workaround is not needed
		// It's kind of expensive (looking up block types) therefore it makes
		// sense to avoid it
		if (config.allowFlying || !config.runflyCheck || player.hasPermission(Permissions.MOVING_FLYING) || player.hasPermission(Permissions.MOVING_RUNFLY))
		{
			return;
		}

		// Get the player-specific stored data that applies here
		final MovingData data = MovingCheck.getData(player);

		final Block block = event.getBlockPlaced();

		if (block == null || !data.runflySetBackPoint.isSet())
		{
			return;
		}

		// Keep some results of "expensive calls
		final Location l = player.getPlayer().getLocation();
		final int playerX = l.getBlockX();
		final int playerY = l.getBlockY();
		final int playerZ = l.getBlockZ();
		final int blockY = block.getY();

		// Was the block below the player?
		if (Math.abs(playerX - block.getX()) <= 1 && Math.abs(playerZ - block.getZ()) <= 1 && playerY - blockY >= 0 && playerY - blockY <= 2)
		{
			// yes
			final int type = CheckUtil.getType(block.getTypeId());
			if (CheckUtil.isSolid(type) || CheckUtil.isLiquid(type))
			{
				if (blockY + 1 >= data.runflySetBackPoint.y)
				{
					data.runflySetBackPoint.y = (blockY + 1);
					data.jumpPhase = 0;
				}
			}
		}
	}

	/**
	 * If a player gets teleported, it may have two reasons. Either it was NoCheat or another plugin. If it was NoCheat,
	 * the target location should match the "data.teleportTo" value.
	 *
	 * On teleports, reset some movement related data that gets invalid
	 *
	 * @param event The PlayerTeleportEvent
	 */
	@EventHandler(priority = EventPriority.HIGHEST)
	public void teleport(final PlayerTeleportEvent event)
	{

		NoCheatPlayer player = plugin.getPlayer(event.getPlayer());
		final MovingData data = MovingCheck.getData(player);

		// If it was a teleport initialized by NoCheat, do it anyway
		// even if another plugin said "no"
		if (data.teleportTo.isSet() && data.teleportTo.equals(event.getTo()))
		{
			event.setCancelled(false);
		}
		else
		{
			// Only if it wasn't NoCheat, drop data from morepackets check.
			// If it was NoCheat, we don't want players to exploit the
			// runfly check teleporting to get rid of the "morepackets"
			// data.
			data.clearMorePacketsData();
		}

		// Always drop data from runfly check, as it always loses its validity
		// after teleports. Always!
		data.teleportTo.reset();
		data.clearRunFlyData();
	}

	/**
	 * Just for security, if a player switches between worlds, reset the runfly and morepackets checks data, because it
	 * is definitely invalid now
	 *
	 * @param event The PlayerChangedWorldEvent
	 */
	@EventHandler(priority = EventPriority.MONITOR)
	public void worldChange(final PlayerChangedWorldEvent event)
	{
		// Maybe this helps with people teleporting through multiverse portals having problems?
		final MovingData data = MovingCheck.getData(plugin.getPlayer(event.getPlayer()));
		data.teleportTo.reset();
		data.clearRunFlyData();
		data.clearMorePacketsData();
	}

	/**
	 * When a player uses a portal, all information related to the moving checks becomes invalid.
	 *
	 * @param event
	 */
	@EventHandler(priority = EventPriority.MONITOR)
	public void portal(final PlayerPortalEvent event)
	{
		final MovingData data = MovingCheck.getData(plugin.getPlayer(event.getPlayer()));
		data.clearMorePacketsData();
		data.clearRunFlyData();
	}

	/**
	 * When a player respawns, all information related to the moving checks becomes invalid.
	 *
	 * @param event
	 */
	@EventHandler(priority = EventPriority.MONITOR)
	public void respawn(final PlayerRespawnEvent event)
	{
		final MovingData data = MovingCheck.getData(plugin.getPlayer(event.getPlayer()));
		data.clearMorePacketsData();
		data.clearRunFlyData();
	}

	/**
	 * When a player moves, he will be checked for various suspicious behaviour.
	 *
	 * @param event The PlayerMoveEvent
	 */
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void move(final PlayerMoveEvent event)
	{

		// Don't care for vehicles
		if (event.getPlayer().isInsideVehicle())
		{
			return;
		}

		// Don't care for movements that are very high distance or to another
		// world (such that it is very likely the event data was modified by
		// another plugin before we got it)
		if (!event.getFrom().getWorld().equals(event.getTo().getWorld()) || event.getFrom().distanceSquared(event.getTo()) > 400)
		{
			return;
		}

		final NoCheatPlayer player = plugin.getPlayer(event.getPlayer());

		final MovingConfig cc = MovingCheck.getConfig(player);
		final MovingData data = MovingCheck.getData(player);

		// Advance various counters and values that change per movement
		// tick. They are needed to decide on how fast a player may
		// move.
		tickVelocities(data);

		// Remember locations
		data.from.set(event.getFrom());
		final Location to = event.getTo();
		data.to.set(to);

		PreciseLocation newTo = null;

		/**
		 * RUNFLY CHECK SECTION *
		 */
		// If the player isn't handled by runfly checks
		if (!cc.runflyCheck || player.hasPermission(Permissions.MOVING_RUNFLY))
		{
			// Just because he is allowed now, doesn't mean he will always
			// be. So forget data about the player related to moving
			data.clearRunFlyData();
		}
		else if (cc.allowFlying || (player.isCreative() && cc.identifyCreativeMode) || player.hasPermission(Permissions.MOVING_FLYING))
		{
			// Only do the limited flying check
			newTo = flyingCheck.check(player, data, cc);
		}
		else
		{
			// Go for the full treatment
			newTo = runningCheck.check(player, data, cc);
		}

		/**
		 * MOREPACKETS CHECK SECTION *
		 */
		if (!cc.morePacketsCheck || player.hasPermission(Permissions.MOVING_MOREPACKETS))
		{
			data.clearMorePacketsData();
		}
		else if (newTo == null)
		{
			newTo = morePacketsCheck.check(player, data, cc);
		}

		// Did one of the check(s) decide we need a new "to"-location?
		if (newTo != null)
		{
			// Compose a new location based on coordinates of "newTo" and
			// viewing direction of "event.getTo()" to allow the player to
			// look somewhere else despite getting pulled back by NoCheat
			event.setTo(new Location(player.getPlayer().getWorld(), newTo.x, newTo.y, newTo.z, to.getYaw(), to.getPitch()));

			// remember where we send the player to
			data.teleportTo.set(newTo);
		}
	}

	/**
	 * Just try to estimate velocities over time Not very precise, but works good enough most of the time.
	 *
	 * @param data
	 */
	private void tickVelocities(MovingData data)
	{

		/**
		 * ****** DO GENERAL DATA MODIFICATIONS ONCE FOR EACH EVENT ****
		 */
		if (data.horizVelocityCounter > 0)
		{
			data.horizVelocityCounter--;
		}
		else if (data.horizFreedom > 0.001)
		{
			data.horizFreedom *= 0.90;
		}

		if (data.vertVelocity <= 0.1)
		{
			data.vertVelocityCounter--;
		}
		if (data.vertVelocityCounter > 0)
		{
			data.vertFreedom += data.vertVelocity;
			data.vertVelocity *= 0.90;
		}
		else if (data.vertFreedom > 0.001)
		{
			// Counter has run out, now reduce the vert freedom over time
			data.vertFreedom *= 0.93;
		}
	}

	/**
	 * Player got a velocity packet. The server can't keep track of actual velocity values (by design), so we have to
	 * try and do that ourselves. Very rough estimates.
	 *
	 * @param event The PlayerVelocityEvent
	 */
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void velocity(final PlayerVelocityEvent event)
	{
		final MovingData data = MovingCheck.getData(plugin.getPlayer(event.getPlayer()));

		final Vector v = event.getVelocity();

		double newVal = v.getY();
		if (newVal >= 0.0D)
		{
			data.vertVelocity += newVal;
			data.vertFreedom += data.vertVelocity;
		}

		data.vertVelocityCounter = 50;

		newVal = Math.sqrt(Math.pow(v.getX(), 2) + Math.pow(v.getZ(), 2));
		if (newVal > 0.0D)
		{
			data.horizFreedom += newVal;
			data.horizVelocityCounter = 30;
		}
	}

	public List<String> getActiveChecks(ConfigurationCacheStore cc)
	{
		LinkedList<String> s = new LinkedList<String>();

		MovingConfig m = MovingCheck.getConfig(cc);

		if (m.runflyCheck)
		{

			if (!m.allowFlying)
			{
				s.add("moving.runfly");
				if (m.sneakingCheck)
				{
					s.add("moving.sneaking");
				}
				if (m.nofallCheck)
				{
					s.add("moving.nofall");
				}
			}
			else
			{
				s.add("moving.flying");
			}

		}
		if (m.morePacketsCheck)
		{
			s.add("moving.morepackets");
		}

		return s;
	}
}
