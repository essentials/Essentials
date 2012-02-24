package com.earth2me.essentials.listener;

import com.earth2me.essentials.Util;
import com.earth2me.essentials.api.IContext;
import com.earth2me.essentials.components.users.IUser;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;


public class EssentialsBlockListener implements Listener
{
	private final transient IContext ess;

	public EssentialsBlockListener(final IContext ess)
	{
		super();
		this.ess = ess;
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onBlockPlace(final BlockPlaceEvent event)
	{
		if (event.isCancelled())
		{
			return;
		}
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
