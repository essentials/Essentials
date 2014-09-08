package com.earth2me.essentials;

import com.earth2me.essentials.utils.LocationUtil;
import java.util.Locale;
import net.ess3.api.IEssentials;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;


public class EssentialsBlockListener implements Listener
{
	private final transient IEssentials ess;

	public EssentialsBlockListener(final IEssentials ess)
	{
		this.ess = ess;
	}

	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void onBlockPlace(final BlockPlaceEvent event)
	{
		final ItemStack is = event.getPlayer().getItemInHand();
		if (is == null)
		{
			return;
		}

		if (is.getType() == Material.MOB_SPAWNER)
		{
			final BlockState blockState = event.getBlockPlaced().getState();
			if (blockState instanceof CreatureSpawner) {
				final CreatureSpawner spawner = (CreatureSpawner) blockState;
				final EntityType type = EntityType.fromId(is.getData().getData());
				if (type != null && Mob.fromBukkitType(type) != null)
				{
					if (ess.getUser(event.getPlayer()).isAuthorized("essentials.spawnerconvert." + Mob.fromBukkitType(type).name().toLowerCase(Locale.ENGLISH)))
					{
						spawner.setSpawnedType(type);
					}
				}
			}
		}

		final User user = ess.getUser(event.getPlayer());
		//Tools can trigger BlockPlaceEvent - only trigger unlimited if we are placing a block or item.
		if (user.hasUnlimited(is) && is.getType().getMaxDurability() == 0 && user.getBase().getGameMode() == GameMode.SURVIVAL)
		{
			final ItemStack amt = is.clone();
			amt.setAmount(1);

			class UnlimitedItemSpawnTask implements Runnable
			{
				@Override
				public void run()
				{
					user.getBase().getInventory().addItem(amt);
					user.getBase().updateInventory();
				}
			}
			ess.scheduleSyncDelayedTask(new UnlimitedItemSpawnTask());
		}
	}
}
