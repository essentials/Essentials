package net.ess3.protect;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockIgniteEvent;


public class EssentialsProtectBlockListener implements Listener
{
	final private IProtect prot;

	public EssentialsProtectBlockListener(final IProtect parent)
	{
		this.prot = parent;
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onBlockIgnite(BlockIgniteEvent event)
	{
		final ProtectHolder settings = prot.getSettings();
		final Block block = event.getBlock();
		if (block.getType() == Material.OBSIDIAN || block.getRelative(BlockFace.DOWN).getType() == Material.OBSIDIAN)
		{
			event.setCancelled(settings.getData().getPrevent().isPortalCreation());
			return;
		}

		if (event.getCause().equals(BlockIgniteEvent.IgniteCause.SPREAD))
		{
			event.setCancelled(settings.getData().getPrevent().isFirespread());
			return;
		}

		if (event.getCause().equals(BlockIgniteEvent.IgniteCause.FLINT_AND_STEEL) && event.getPlayer() != null)
		{
			event.setCancelled(Permissions.USEFLINTSTEEL.isAuthorized(event.getPlayer()));
			return;
		}

		if (event.getCause().equals(BlockIgniteEvent.IgniteCause.LAVA))
		{
			event.setCancelled(settings.getData().getPrevent().isLavaFirespread());
			return;
		}
		if (event.getCause().equals(BlockIgniteEvent.IgniteCause.LIGHTNING))
		{
			event.setCancelled(settings.getData().getPrevent().isLightningFirespread());
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onBlockFromTo(final BlockFromToEvent event)
	{
		final ProtectHolder settings = prot.getSettings();
		final Block block = event.getBlock();
		if (block.getType() == Material.WATER || block.getType() == Material.STATIONARY_WATER)
		{
			event.setCancelled(settings.getData().getPrevent().isWaterFlow());
			return;
		}

		if (block.getType() == Material.LAVA || block.getType() == Material.STATIONARY_LAVA)
		{
			event.setCancelled(settings.getData().getPrevent().isLavaFlow());
		}
		// TODO: Test if this still works
			/*
		 * if (block.getType() == Material.AIR) {
		 * event.setCancelled(prot.getSettingBool(ProtectConfig.prevent_water_bucket_flow)); return; }
		 */
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onBlockBurn(final BlockBurnEvent event)
	{
		final ProtectHolder settings = prot.getSettings();

		if (settings.getData().getPrevent().isFirespread())
		{
			event.setCancelled(true);
		}
	}
}