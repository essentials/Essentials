package net.ess3.signs.listeners;

import java.util.logging.Level;
import java.util.logging.Logger;
import net.ess3.api.IEssentials;
import net.ess3.api.IUser;
import net.ess3.permissions.Permissions;
import net.ess3.signs.EssentialsSign;
import net.ess3.signs.ISignsPlugin;
import net.ess3.signs.Signs;
import net.ess3.utils.FormatUtil;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;


public class SignBlockListener implements Listener
{
	private final IEssentials ess;
	private final ISignsPlugin plugin;
	private final static int WALL_SIGN = Material.WALL_SIGN.getId();
	private final static int SIGN_POST = Material.SIGN_POST.getId();

	public SignBlockListener(final IEssentials ess, final ISignsPlugin plugin)
	{
		this.ess = ess;
		this.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onBlockBreak(final BlockBreakEvent event)
	{
		if (plugin.getSettings().areSignsDisabled())
		{
			return;
		}

		if (protectSignsAndBlocks(event.getBlock(), event.getPlayer()))
		{
			event.setCancelled(true);
		}
	}

	public boolean protectSignsAndBlocks(final Block block, final Player player)
	{
		// prevent any signs be broken by destroying the block they are attached to
		if (EssentialsSign.checkIfBlockBreaksSigns(block))
		{
			ess.getLogger().log(Level.INFO, "Prevented that a block was broken next to a sign.");
			return true;
		}

		final int mat = block.getTypeId();
		if (mat == SIGN_POST || mat == WALL_SIGN)
		{
			final Sign csign = (Sign)block.getState();

			for (EssentialsSign sign : plugin.getSettings().getEnabledSigns())
			{
				if (csign.getLine(0).equalsIgnoreCase(sign.getSuccessName()) && !sign.onSignBreak(block, player, ess))
				{
					return true;
				}
			}
		}
		for (EssentialsSign sign : plugin.getSettings().getEnabledSigns())
		{
			if (sign.getBlocks().contains(block.getType()) && !sign.onBlockBreak(block, player, ess))
			{
				ess.getLogger().log(Level.INFO, "A block was protected by a sign.");
				return true;
			}
		}
		return false;
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onSignChange(final SignChangeEvent event)
	{
		if (plugin.getSettings().areSignsDisabled())
		{
			return;
		}
		IUser user = ess.getUserMap().getUser(event.getPlayer());

		for (int i = 0; i < 4; i++)
		{
			event.setLine(i, FormatUtil.formatString(user, Permissions.SIGNS, event.getLine(i)));
		}

		for (Signs signs : Signs.values())
		{
			final EssentialsSign sign = signs.getSign();
			if (event.getLine(0).equalsIgnoreCase(sign.getSuccessName()))
			{
				event.setCancelled(true);
				return;
			}
			if (event.getLine(0).equalsIgnoreCase(sign.getTemplateName()) && !sign.onSignCreate(event, ess))
			{
				event.setCancelled(true);
				return;
			}
		}
	}

	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void onBlockPlace(final BlockPlaceEvent event)
	{
		if (plugin.getSettings().areSignsDisabled())
		{
			return;
		}

		final Block against = event.getBlockAgainst();
		if ((against.getTypeId() == WALL_SIGN || against.getTypeId() == SIGN_POST) && EssentialsSign.isValidSign(new EssentialsSign.BlockSign(against)))
		{
			event.setCancelled(true);
			return;
		}
		final Block block = event.getBlock();
		if (block.getTypeId() == WALL_SIGN || block.getTypeId() == SIGN_POST)
		{
			return;
		}
		for (Signs signs : Signs.values())
		{
			final EssentialsSign sign = signs.getSign();
			if (sign.getBlocks().contains(block.getType()) && !sign.onBlockPlace(block, event.getPlayer(), ess))
			{
				event.setCancelled(true);
				return;
			}
		}
	}

	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void onBlockBurn(final BlockBurnEvent event)
	{
		if (plugin.getSettings().areSignsDisabled())
		{
			return;
		}

		final Block block = event.getBlock();
		if (((block.getTypeId() == WALL_SIGN || block.getTypeId() == SIGN_POST) && EssentialsSign.isValidSign(
			 new EssentialsSign.BlockSign(block))) || EssentialsSign.checkIfBlockBreaksSigns(block))
		{
			event.setCancelled(true);
			return;
		}
		for (EssentialsSign sign : plugin.getSettings().getEnabledSigns())
		{
			if (sign.getBlocks().contains(block.getType()) && !sign.onBlockBurn(block, ess))
			{
				event.setCancelled(true);
				return;
			}
		}
	}

	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void onBlockIgnite(final BlockIgniteEvent event)
	{
		if (plugin.getSettings().areSignsDisabled())
		{
			return;
		}

		final Block block = event.getBlock();
		if (((block.getTypeId() == WALL_SIGN || block.getTypeId() == SIGN_POST) && EssentialsSign.isValidSign(
			 new EssentialsSign.BlockSign(block))) || EssentialsSign.checkIfBlockBreaksSigns(block))
		{
			event.setCancelled(true);
			return;
		}
		for (EssentialsSign sign : plugin.getSettings().getEnabledSigns())
		{
			if (sign.getBlocks().contains(block.getType()) && !sign.onBlockIgnite(block, ess))
			{
				event.setCancelled(true);
				return;
			}
		}
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onBlockPistonExtend(final BlockPistonExtendEvent event)
	{
		if (plugin.getSettings().areSignsDisabled())
		{
			return;
		}

		for (Block block : event.getBlocks())
		{
			if (((block.getTypeId() == WALL_SIGN || block.getTypeId() == SIGN_POST) && EssentialsSign.isValidSign(
				 new EssentialsSign.BlockSign(block))) || EssentialsSign.checkIfBlockBreaksSigns(block))
			{
				event.setCancelled(true);
				return;
			}
			for (EssentialsSign sign : plugin.getSettings().getEnabledSigns())
			{
				if (sign.getBlocks().contains(block.getType()) && !sign.onBlockPush(block, ess))
				{
					event.setCancelled(true);
					return;
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onBlockPistonRetract(final BlockPistonRetractEvent event)
	{
		if (plugin.getSettings().areSignsDisabled())
		{
			return;
		}

		if (event.isSticky())
		{
			final Block block = event.getBlock();
			if (((block.getTypeId() == WALL_SIGN || block.getTypeId() == SIGN_POST) && EssentialsSign.isValidSign(
				 new EssentialsSign.BlockSign(block))) || EssentialsSign.checkIfBlockBreaksSigns(block))
			{
				event.setCancelled(true);
				return;
			}
			for (EssentialsSign sign : plugin.getSettings().getEnabledSigns())
			{
				if (sign.getBlocks().contains(block.getType()) && !sign.onBlockPush(block, ess))
				{
					event.setCancelled(true);
					return;
				}
			}
		}
	}
}
