package com.earth2me.essentials.anticheat.checks.inventory;

import com.earth2me.essentials.anticheat.EventManager;
import com.earth2me.essentials.anticheat.NoCheat;
import com.earth2me.essentials.anticheat.NoCheatPlayer;
import com.earth2me.essentials.anticheat.checks.CheckUtil;
import com.earth2me.essentials.anticheat.config.ConfigurationCacheStore;
import com.earth2me.essentials.anticheat.config.Permissions;
import java.util.LinkedList;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;


/**
 * Central location to listen to events that are relevant for the inventory checks
 *
 */
public class InventoryCheckListener implements Listener, EventManager
{
	private final DropCheck dropCheck;
	private final InstantBowCheck instantBowCheck;
	private final InstantEatCheck instantEatCheck;
	private final NoCheat plugin;

	public InventoryCheckListener(NoCheat plugin)
	{

		this.dropCheck = new DropCheck(plugin);
		this.instantBowCheck = new InstantBowCheck(plugin);
		this.instantEatCheck = new InstantEatCheck(plugin);

		this.plugin = plugin;
	}

	/**
	 * We listen to DropItem Event for the dropCheck
	 *
	 * @param event The PlayerDropItem Event
	 */
	@EventHandler(priority = EventPriority.LOWEST)
	protected void handlePlayerDropItemEvent(final PlayerDropItemEvent event)
	{

		if (event.isCancelled() || event.getPlayer().isDead())
		{
			return;
		}

		boolean cancelled = false;

		final NoCheatPlayer player = plugin.getPlayer(event.getPlayer());
		final InventoryConfig cc = InventoryCheck.getConfig(player);
		final InventoryData data = InventoryCheck.getData(player);

		// If it should be executed, do it
		if (cc.dropCheck && !player.hasPermission(Permissions.INVENTORY_DROP))
		{
			cancelled = dropCheck.check(player, data, cc);
		}

		if (cancelled)
		{
			// Cancelling drop events is not save (in certain circumstances
			// items will disappear completely). So don't do it and kick
			// players instead by default
			// event.setCancelled(true);
		}
	}

	/**
	 * We listen to PlayerInteractEvent for the instantEat and instantBow checks
	 *
	 * @param event The PlayerInteractEvent
	 */
	@EventHandler(priority = EventPriority.LOWEST)
	public void interact(final PlayerInteractEvent event)
	{

		// Only interested in right-clicks while holding an item
		if (!event.hasItem() || !(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK))
		{
			return;
		}

		NoCheatPlayer player = plugin.getPlayer(event.getPlayer());
		final InventoryData data = InventoryCheck.getData(player);

		if (event.getItem().getType() == Material.BOW)
		{
			// It was a bow, the player starts to pull the string
			// Remember this time
			data.lastBowInteractTime = System.currentTimeMillis();
		}
		else if (CheckUtil.isFood(event.getItem()))
		{
			// It was food, the player starts to eat some food
			// Remember this time and the type of food
			data.foodMaterial = event.getItem().getType();
			data.lastEatInteractTime = System.currentTimeMillis();
		}
		else
		{
			// Nothing that we are interested in, reset data
			data.lastBowInteractTime = 0;
			data.lastEatInteractTime = 0;
			data.foodMaterial = null;
		}
	}

	/**
	 * We listen to FoodLevelChange Event because Bukkit doesn't provide a PlayerFoodEating Event (or whatever it would
	 * be called).
	 *
	 * @param event The FoodLevelChangeEvent
	 */
	@EventHandler(priority = EventPriority.LOWEST)
	public void foodchanged(final FoodLevelChangeEvent event)
	{
		// Only if a player ate food
		if (!event.isCancelled() && event.getEntity() instanceof Player)
		{
			final NoCheatPlayer player = plugin.getPlayer((Player)event.getEntity());
			final InventoryConfig cc = InventoryCheck.getConfig(player);
			final InventoryData data = InventoryCheck.getData(player);

			// Only if he should get checked
			if (cc.eatCheck && !player.hasPermission(Permissions.INVENTORY_INSTANTEAT))
			{

				boolean cancelled = instantEatCheck.check(player, event, data, cc);

				// The check requested the foodlevelchange to get cancelled
				event.setCancelled(cancelled);
			}

			// Forget the food material, as the info is no longer needed
			data.foodMaterial = null;
		}

	}

	/**
	 * We listen to EntityShootBowEvent for the instantbow check
	 *
	 * @param event The EntityShootBowEvent
	 */
	@EventHandler(priority = EventPriority.LOWEST)
	public void bowfired(final EntityShootBowEvent event)
	{
		// Only if a player shot the arrow
		if (!event.isCancelled() && event.getEntity() instanceof Player)
		{
			final NoCheatPlayer player = plugin.getPlayer((Player)event.getEntity());
			final InventoryConfig cc = InventoryCheck.getConfig(player);

			// Only if he should get checked
			if (cc.bowCheck && !player.hasPermission(Permissions.INVENTORY_INSTANTBOW))
			{
				final InventoryData data = InventoryCheck.getData(player);
				boolean cancelled = instantBowCheck.check(player, event, data, cc);

				// The check requested the bowshooting to get cancelled
				event.setCancelled(cancelled);
			}
		}
	}

	public List<String> getActiveChecks(ConfigurationCacheStore cc)
	{
		LinkedList<String> s = new LinkedList<String>();

		InventoryConfig i = InventoryCheck.getConfig(cc);
		if (i.dropCheck)
		{
			s.add("inventory.dropCheck");
		}
		if (i.bowCheck)
		{
			s.add("inventory.instantbow");
		}
		if (i.eatCheck)
		{
			s.add("inventory.instanteat");
		}
		return s;
	}
}
