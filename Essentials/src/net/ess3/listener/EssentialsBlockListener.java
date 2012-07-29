package net.ess3.listener;

import net.ess3.api.IEssentials;
import net.ess3.api.IUser;
import net.ess3.api.ondemand.OnDemand;
import net.ess3.api.server.Block;
import net.ess3.api.server.ItemStack;
import net.ess3.api.server.events.EventListener;
import net.ess3.api.server.events.EventPriority;
import net.ess3.api.server.events.EventType;


public class EssentialsBlockListener extends EventListener
{
	private final transient IEssentials ess;

	public EssentialsBlockListener(final IEssentials ess)
	{
		super();
		this.ess = ess;
		register(EventType.PLACE_BLOCK, EventPriority.LOW, true);
	}

	@Override
	public boolean onBlockPlace(final Block placedBlock, final OnDemand<IUser> user)
	{
		final ItemStack itemstack = placedBlock.convertToItem();
		if (placedBlock == null)
		{
			return true;
		}

		final boolean unlimitedForUser = user.get().getData().hasUnlimited(itemstack.getType());
		if (unlimitedForUser && user.get().isInSurvivalMode())
		{
			ess.getPlugin().scheduleSyncDelayedTask(
					new Runnable()
					{
						@Override
						public void run()
						{
							user.get().getInventory().addItem(itemstack);
							user.get().updateInventory();
						}
					});
		}
		return true;
	}
}
