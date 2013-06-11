package com.earth2me.essentials;

import com.earth2me.essentials.utils.LocationUtil;
import org.bukkit.GameMode;
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
		// Do not rely on getItemInHand();
		// http://leaky.bukkit.org/issues/663
		final ItemStack is = LocationUtil.convertBlockToItem(event.getBlockPlaced());
		if (is == null)
		{
			return;
		}
		final User user = ess.getUser(event.getPlayer());
		if (user.hasUnlimited(is) && user.getGameMode() == GameMode.SURVIVAL)
		{
			ess.scheduleSyncDelayedTask(
					new Runnable()
					{
						@Override
						public void run()
						{
							user.getInventory().addItem(is);
							user.updateInventory();
						}
					});
		}
	}
}
