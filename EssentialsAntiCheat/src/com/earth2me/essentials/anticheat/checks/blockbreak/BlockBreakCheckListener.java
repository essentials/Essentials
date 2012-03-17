package com.earth2me.essentials.anticheat.checks.blockbreak;

import com.earth2me.essentials.anticheat.EventManager;
import com.earth2me.essentials.anticheat.NoCheat;
import com.earth2me.essentials.anticheat.NoCheatPlayer;
import com.earth2me.essentials.anticheat.config.ConfigurationCacheStore;
import com.earth2me.essentials.anticheat.config.Permissions;
import java.util.LinkedList;
import java.util.List;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerInteractEvent;


/**
 * Central location to listen to events that are relevant for the blockbreak checks
 *
 */
public class BlockBreakCheckListener implements Listener, EventManager
{
	private final NoswingCheck noswingCheck;
	private final ReachCheck reachCheck;
	private final DirectionCheck directionCheck;
	private final NoCheat plugin;

	public BlockBreakCheckListener(NoCheat plugin)
	{

		noswingCheck = new NoswingCheck(plugin);
		reachCheck = new ReachCheck(plugin);
		directionCheck = new DirectionCheck(plugin);

		this.plugin = plugin;
	}

	/**
	 * We listen to blockBreak events for obvious reasons
	 *
	 * @param event The blockbreak event
	 */
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void blockBreak(final BlockBreakEvent event)
	{
		boolean cancelled = false;

		final NoCheatPlayer player = plugin.getPlayer(event.getPlayer());
		final BlockBreakConfig cc = BlockBreakCheck.getConfig(player);
		final BlockBreakData data = BlockBreakCheck.getData(player);

		// Remember the location of the block that will be broken
		data.brokenBlockLocation.set(event.getBlock());

		// Only if the block got damaged directly before, do the check(s)
		if (!data.brokenBlockLocation.equals(data.lastDamagedBlock))
		{
			// Something caused a blockbreak event that's not from the player
			// Don't check it at all
			data.lastDamagedBlock.reset();
			return;
		}

		// Now do the actual checks, if still needed. It's a good idea to make
		// computationally cheap checks first, because it may save us from
		// doing the computationally expensive checks.

		// First NoSwing: Did the arm of the player move before breaking this
		// block?
		if (cc.noswingCheck && !player.hasPermission(Permissions.BLOCKBREAK_NOSWING))
		{
			cancelled = noswingCheck.check(player, data, cc);
		}

		// Second Reach: Is the block really in reach distance
		if (!cancelled && cc.reachCheck && !player.hasPermission(Permissions.BLOCKBREAK_REACH))
		{
			cancelled = reachCheck.check(player, data, cc);
		}

		// Third Direction: Did the player look at the block at all
		if (!cancelled && cc.directionCheck && !player.hasPermission(Permissions.BLOCKBREAK_DIRECTION))
		{
			cancelled = directionCheck.check(player, data, cc);
		}

		// At least one check failed and demanded to cancel the event
		if (cancelled)
		{
			event.setCancelled(cancelled);
		}
	}

	/**
	 * We listen to BlockDamage events to grab the information if it has been an "insta-break". That info may come in
	 * handy later.
	 *
	 * @param event The BlockDamage event
	 */
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void blockHit(final BlockDamageEvent event)
	{
		NoCheatPlayer player = plugin.getPlayer(event.getPlayer());
		BlockBreakData data = BlockBreakCheck.getData(player);

		// Only interested in insta-break events here
		if (event.getInstaBreak())
		{
			// Remember this location. We handle insta-breaks slightly
			// different in some of the blockbreak checks.
			data.instaBrokenBlockLocation.set(event.getBlock());
		}

	}

	/**
	 * We listen to BlockInteract events to be (at least in many cases) able to distinguish between blockbreak events
	 * that were triggered by players actually digging and events that were artificially created by plugins.
	 *
	 * @param event
	 */
	@EventHandler(priority = EventPriority.MONITOR)
	public void blockInteract(final PlayerInteractEvent event)
	{

		if (event.getClickedBlock() == null)
		{
			return;
		}

		NoCheatPlayer player = plugin.getPlayer(event.getPlayer());
		BlockBreakData data = BlockBreakCheck.getData(player);
		// Remember this location. Only blockbreakevents for this specific
		// block will be handled at all
		data.lastDamagedBlock.set(event.getClickedBlock());
	}

	/**
	 * We listen to PlayerAnimationEvent because it is (currently) equivalent to "player swings arm" and we want to
	 * check if he did that between blockbreaks.
	 *
	 * @param event The PlayerAnimation Event
	 */
	@EventHandler(priority = EventPriority.MONITOR)
	public void armSwing(final PlayerAnimationEvent event)
	{
		// Just set a flag to true when the arm was swung
		BlockBreakCheck.getData(plugin.getPlayer(event.getPlayer())).armswung = true;
	}

	public List<String> getActiveChecks(ConfigurationCacheStore cc)
	{
		LinkedList<String> s = new LinkedList<String>();

		BlockBreakConfig bb = BlockBreakCheck.getConfig(cc);

		if (bb.directionCheck)
		{
			s.add("blockbreak.direction");
		}
		if (bb.reachCheck)
		{
			s.add("blockbreak.reach");
		}
		if (bb.noswingCheck)
		{
			s.add("blockbreak.noswing");
		}

		return s;
	}
}
