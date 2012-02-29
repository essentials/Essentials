package com.earth2me.essentials.listeners;

import com.earth2me.essentials.Util;
import com.earth2me.essentials.api.IContext;
import com.earth2me.essentials.components.users.IUserComponent;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;


public class EssentialsBlockListener implements Listener
{
	private final transient IContext context;

	public EssentialsBlockListener(final IContext ess)
	{
		super();
		this.context = ess;
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
		final IUserComponent user = context.getUser(event.getPlayer());
		final boolean unlimitedForUser = user.hasUnlimited(itemstack.getType());
		if (unlimitedForUser && user.getGameMode() == GameMode.SURVIVAL)
		{
			context.getScheduler().scheduleSyncDelayedTask(
					new Runnable()
					{
						@Override
						@SuppressWarnings("deprecation")
						public void run()
						{
							user.getInventory().addItem(itemstack);
							user.updateInventory();
						}
					});
		}
	}
}
