package com.earth2me.essentials.signs;

import com.earth2me.essentials.api.IEssentials;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityExplodeEvent;


public class SignEntityListener implements Listener
{
	private final transient IEssentials ess;
	private final transient ISignsPlugin plugin;

	public SignEntityListener(final IEssentials ess, final ISignsPlugin plugin)
	{
		this.ess = ess;
		this.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onEntityExplode(final EntityExplodeEvent event)
	{
		for (Block block : event.blockList())
		{
			if (((block.getTypeId() == Material.WALL_SIGN.getId()
				  || block.getTypeId() == Material.SIGN_POST.getId())
				 && EssentialsSign.isValidSign(new EssentialsSign.BlockSign(block)))
				|| EssentialsSign.checkIfBlockBreaksSigns(block))
			{
				event.setCancelled(true);
				return;
			}
			for (EssentialsSign sign : plugin.getSettings().getEnabledSigns())
			{
				if (sign.getBlocks().contains(block.getType()))
				{
					event.setCancelled(!sign.onBlockExplode(block, ess));
					return;
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void onEntityChangeBlock(final EntityChangeBlockEvent event)
	{
		if (plugin.getSettings().areSignsDisabled())
		{
			return;
		}

		final Block block = event.getBlock();
		if (((block.getTypeId() == Material.WALL_SIGN.getId()
			  || block.getTypeId() == Material.SIGN_POST.getId())
			 && EssentialsSign.isValidSign(new EssentialsSign.BlockSign(block)))
			|| EssentialsSign.checkIfBlockBreaksSigns(block))
		{
			event.setCancelled(true);
			return;
		}
		for (EssentialsSign sign : plugin.getSettings().getEnabledSigns())
		{
			if (sign.getBlocks().contains(block.getType())
				&& !sign.onBlockBreak(block, ess))
			{
				event.setCancelled(true);
				return;
			}
		}
	}
}
