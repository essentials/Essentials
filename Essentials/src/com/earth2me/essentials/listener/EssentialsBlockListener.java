package com.earth2me.essentials.listener;

import com.earth2me.essentials.utils.Util;
import com.earth2me.essentials.api.IEssentials;
import com.earth2me.essentials.api.IUser;
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
		super();
		this.ess = ess;
	}

	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void onBlockPlace(final BlockPlaceEvent event)
	{
		// Do not rely on getItemInHand();
		// http://leaky.bukkit.org/issues/663		
		final ItemStack itemstack = Util.convertBlockToItem(event.getBlockPlaced());
		if (itemstack == null)
		{
			return;
		}
		final IUser user = ess.getUser(event.getPlayer());
		final boolean unlimitedForUser = user.getData().hasUnlimited(itemstack.getType());
		if (unlimitedForUser && user.getGameMode() == GameMode.SURVIVAL)
		{
			ess.scheduleSyncDelayedTask(
					new Runnable()
					{
						@Override
						public void run()
						{
							user.getInventory().addItem(itemstack);
							user.updateInventory();
						}
					});
		}
	}
}
