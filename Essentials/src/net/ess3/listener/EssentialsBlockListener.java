package net.ess3.listener;

import net.ess3.api.IEssentials;
import net.ess3.api.IUser;
import net.ess3.bukkit.BukkitMaterial;
import org.bukkit.GameMode;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;


public class EssentialsBlockListener implements Listener
{
	private final IEssentials ess;

	public EssentialsBlockListener(final IEssentials ess)
	{
		this.ess = ess;
	}

	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void onBlockPlace(final BlockPlaceEvent event)
	{
		final Block block = event.getBlockPlaced();
		final ItemStack itemstack = BukkitMaterial.convertBlockToItem(block.getType(), block.getData());
		if (itemstack == null)
		{
			return;
		}

		final Player player = event.getPlayer();
		final IUser user = ess.getUserMap().getUser(player);

		final boolean unlimitedForUser = user.getData().hasUnlimited(itemstack.getType());
		if (unlimitedForUser && player.getGameMode() != GameMode.CREATIVE)
		{
			ess.getPlugin().scheduleSyncDelayedTask(
					new Runnable()
					{
						@Override
						public void run()
						{
							player.getInventory().addItem(itemstack);
							player.updateInventory();
						}
					});
		}
	}
}
