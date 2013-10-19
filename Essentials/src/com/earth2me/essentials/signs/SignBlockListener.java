package com.earth2me.essentials.signs;

import com.earth2me.essentials.User;
import com.earth2me.essentials.utils.FormatUtil;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.ess3.api.IEssentials;
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
	private final transient IEssentials ess;
	private final static Logger LOGGER = Logger.getLogger("Minecraft");
	private final static Material WALL_SIGN = Material.WALL_SIGN;
	private final static Material SIGN_POST = Material.SIGN_POST;

	public SignBlockListener(IEssentials ess)
	{
		this.ess = ess;
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onBlockBreak(final BlockBreakEvent event)
	{
		if (ess.getSettings().areSignsDisabled())
		{
			event.getHandlers().unregister(this);
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
			LOGGER.log(Level.INFO, "Prevented that a block was broken next to a sign.");
			return true;
		}

		final Material mat = block.getType();
		if (mat == SIGN_POST || mat == WALL_SIGN)
		{
			final Sign csign = (Sign)block.getState();

			for (EssentialsSign sign : ess.getSettings().enabledSigns())
			{
				if (csign.getLine(0).equalsIgnoreCase(sign.getSuccessName())
					&& !sign.onSignBreak(block, player, ess))
				{
					return true;
				}
			}
		}

		for (EssentialsSign sign : ess.getSettings().enabledSigns())
		{
			if (sign.areHeavyEventRequired() && sign.getBlocks().contains(block.getType())
				&& !sign.onBlockBreak(block, player, ess))
			{
				LOGGER.log(Level.INFO, "A block was protected by a sign.");
				return true;
			}
		}
		return false;
	}

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onSignChange2(final SignChangeEvent event)
	{
		if (ess.getSettings().areSignsDisabled())
		{
			event.getHandlers().unregister(this);
			return;
		}
		User user = ess.getUser(event.getPlayer());

		for (int i = 0; i < 4; i++)
		{
			event.setLine(i, FormatUtil.formatString(user, "essentials.signs", event.getLine(i)));
		}

		final String topLine = event.getLine(0);
		for (Signs signs : Signs.values())
		{
			final EssentialsSign sign = signs.getSign();
			if (topLine.equalsIgnoreCase(sign.getSuccessName()))
			{
				event.setLine(0, FormatUtil.stripFormat(topLine));
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onSignChange(final SignChangeEvent event)
	{
		if (ess.getSettings().areSignsDisabled())
		{
			event.getHandlers().unregister(this);
			return;
		}

		for (EssentialsSign sign : ess.getSettings().enabledSigns())
		{
			if (event.getLine(0).equalsIgnoreCase(sign.getSuccessName()))
			{
				event.setCancelled(true);
				return;
			}
			if (event.getLine(0).equalsIgnoreCase(sign.getTemplateName())
				&& !sign.onSignCreate(event, ess))
			{
				event.setCancelled(true);
				return;
			}
		}
	}

	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void onBlockPlace(final BlockPlaceEvent event)
	{
		if (ess.getSettings().areSignsDisabled())
		{
			event.getHandlers().unregister(this);
			return;
		}

		final Block against = event.getBlockAgainst();
		if ((against.getType() == WALL_SIGN
			 || against.getType() == SIGN_POST)
			&& EssentialsSign.isValidSign(new EssentialsSign.BlockSign(against)))
		{
			event.setCancelled(true);
			return;
		}
		final Block block = event.getBlock();
		if (block.getType() == WALL_SIGN
			|| block.getType() == SIGN_POST)
		{
			return;
		}
		for (EssentialsSign sign : ess.getSettings().enabledSigns())
		{
			if (sign.areHeavyEventRequired() && sign.getBlocks().contains(block.getType())
				&& !sign.onBlockPlace(block, event.getPlayer(), ess))
			{
				event.setCancelled(true);
				return;
			}
		}
	}

	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void onBlockBurn(final BlockBurnEvent event)
	{
		if (ess.getSettings().areSignsDisabled())
		{
			event.getHandlers().unregister(this);
			return;
		}

		final Block block = event.getBlock();
		if (((block.getType() == WALL_SIGN
			  || block.getType() == SIGN_POST)
			 && EssentialsSign.isValidSign(new EssentialsSign.BlockSign(block)))
			|| EssentialsSign.checkIfBlockBreaksSigns(block))
		{
			event.setCancelled(true);
			return;
		}
		for (EssentialsSign sign : ess.getSettings().enabledSigns())
		{
			if (sign.areHeavyEventRequired() && sign.getBlocks().contains(block.getType())
				&& !sign.onBlockBurn(block, ess))
			{
				event.setCancelled(true);
				return;
			}
		}
	}

	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void onBlockIgnite(final BlockIgniteEvent event)
	{
		if (ess.getSettings().areSignsDisabled())
		{
			event.getHandlers().unregister(this);
			return;
		}

		final Block block = event.getBlock();
		if (((block.getType() == WALL_SIGN
			  || block.getType() == SIGN_POST)
			 && EssentialsSign.isValidSign(new EssentialsSign.BlockSign(block)))
			|| EssentialsSign.checkIfBlockBreaksSigns(block))
		{
			event.setCancelled(true);
			return;
		}
		for (EssentialsSign sign : ess.getSettings().enabledSigns())
		{
			if (sign.areHeavyEventRequired() && sign.getBlocks().contains(block.getType())
				&& !sign.onBlockIgnite(block, ess))
			{
				event.setCancelled(true);
				return;
			}
		}
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onBlockPistonExtend(final BlockPistonExtendEvent event)
	{
		if (ess.getSettings().areSignsDisabled())
		{
			event.getHandlers().unregister(this);
			return;
		}

		for (Block block : event.getBlocks())
		{
			if (((block.getType() == WALL_SIGN
				  || block.getType() == SIGN_POST)
				 && EssentialsSign.isValidSign(new EssentialsSign.BlockSign(block)))
				|| EssentialsSign.checkIfBlockBreaksSigns(block))
			{
				event.setCancelled(true);
				return;
			}
			for (EssentialsSign sign : ess.getSettings().enabledSigns())
			{
				if (sign.areHeavyEventRequired() && sign.getBlocks().contains(block.getType())
					&& !sign.onBlockPush(block, ess))
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
		if (ess.getSettings().areSignsDisabled())
		{
			event.getHandlers().unregister(this);
			return;
		}

		if (event.isSticky())
		{
			final Block block = event.getBlock();
			if (((block.getType() == WALL_SIGN
				  || block.getType() == SIGN_POST)
				 && EssentialsSign.isValidSign(new EssentialsSign.BlockSign(block)))
				|| EssentialsSign.checkIfBlockBreaksSigns(block))
			{
				event.setCancelled(true);
				return;
			}
			for (EssentialsSign sign : ess.getSettings().enabledSigns())
			{
				if (sign.areHeavyEventRequired() && sign.getBlocks().contains(block.getType())
					&& !sign.onBlockPush(block, ess))
				{
					event.setCancelled(true);
					return;
				}
			}
		}
	}
}
