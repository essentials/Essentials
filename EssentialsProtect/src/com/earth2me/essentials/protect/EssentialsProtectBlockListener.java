package com.earth2me.essentials.protect;

import com.earth2me.essentials.IEssentials;
import com.earth2me.essentials.User;
import com.earth2me.essentials.Util;
import com.earth2me.essentials.protect.data.IProtectedBlock;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPlaceEvent;


public class EssentialsProtectBlockListener extends BlockListener
{
	final private transient IProtect prot;
	final private transient IEssentials ess;

	public EssentialsProtectBlockListener(final IProtect parent)
	{
		this.prot = parent;
		this.ess = prot.getEssentials();
	}

	@Override
	public void onBlockPlace(final BlockPlaceEvent event)
	{
		if (event.isCancelled())
		{
			return;
		}

		final User user = ess.getUser(event.getPlayer());

		if (prot.getSettingBool(ProtectConfig.disable_build) && !user.canBuild())
		{
			event.setCancelled(true);
			return;
		}

		final Block blockPlaced = event.getBlockPlaced();
		final int id = blockPlaced.getTypeId();

		if (prot.checkProtectionItems(ProtectConfig.blacklist_placement, id) && !user.isAuthorized("essentials.protect.exemptplacement"))
		{
			event.setCancelled(true);
			return;
		}

		if (prot.checkProtectionItems(ProtectConfig.alert_on_placement, id))
		{
			prot.alert(user, blockPlaced.getType().toString(), Util.i18n("alertPlaced"));
		}

		final Block below = blockPlaced.getFace(BlockFace.DOWN);
		if (below.getType() == Material.RAILS
			&& prot.getSettingBool(ProtectConfig.prevent_block_on_rail)
			&& prot.getStorage().isProtected(below, user.getName()))
		{
			event.setCancelled(true);
			return;
		}

		final List<Block> protect = new ArrayList<Block>();
		if (blockPlaced.getType() == Material.RAILS
			&& prot.getSettingBool(ProtectConfig.protect_rails)
			&& user.isAuthorized("essentials.protect"))
		{
			protect.add(blockPlaced);
			if (prot.getSettingBool(ProtectConfig.protect_below_rails))
			{
				protect.add(blockPlaced.getFace(BlockFace.DOWN));
			}
		}
		if ((blockPlaced.getType() == Material.SIGN_POST || blockPlaced.getType() == Material.WALL_SIGN)
			&& prot.getSettingBool(ProtectConfig.protect_signs)
			&& user.isAuthorized("essentials.protect"))
		{
			protect.add(blockPlaced);
			if (prot.getSettingBool(ProtectConfig.protect_against_signs))
			{
				protect.add(event.getBlockAgainst());
			}
		}
		for (Block block : protect)
		{
			prot.getStorage().protectBlock(block, user.getName());
		}
	}

	@Override
	public void onBlockIgnite(BlockIgniteEvent event)
	{
		Block block = event.getBlock();
		if (block.getType() == Material.RAILS
			&& prot.getSettingBool(ProtectConfig.protect_rails))
		{
			event.setCancelled(true);
			return;
		}
		if ((block.getType() == Material.WALL_SIGN || block.getType() == Material.SIGN_POST)
			&& prot.getSettingBool(ProtectConfig.protect_signs))
		{
			event.setCancelled(true);
			return;
		}
		if (event.getBlock().getType() == Material.OBSIDIAN
			|| event.getBlock().getFace(BlockFace.DOWN).getType() == Material.OBSIDIAN)
		{
			event.setCancelled(prot.getSettingBool(ProtectConfig.prevent_portal_creation));
			return;
		}

		if (event.getCause().equals(BlockIgniteEvent.IgniteCause.SPREAD))
		{
			event.setCancelled(prot.getSettingBool(ProtectConfig.prevent_fire_spread));
			return;
		}

		if (event.getCause().equals(BlockIgniteEvent.IgniteCause.FLINT_AND_STEEL))
		{
			event.setCancelled(prot.getSettingBool(ProtectConfig.prevent_flint_fire));
			return;
		}

		if (event.getCause().equals(BlockIgniteEvent.IgniteCause.LAVA))
		{
			event.setCancelled(prot.getSettingBool(ProtectConfig.prevent_lava_fire_spread));
			return;
		}
		if (event.getCause().equals(BlockIgniteEvent.IgniteCause.LIGHTNING))
		{
			event.setCancelled(prot.getSettingBool(ProtectConfig.prevent_lightning_fire_spread));
			return;
		}
	}

	@Override
	public void onBlockFromTo(final BlockFromToEvent event)
	{
		if (event.isCancelled())
		{
			return;
		}
		final Block toBlock = event.getToBlock();
		if (toBlock.getType() == Material.RAILS
			&& prot.getSettingBool(ProtectConfig.protect_rails))
		{
			event.setCancelled(true);
			return;
		}
		if ((toBlock.getType() == Material.WALL_SIGN || toBlock.getType() == Material.SIGN_POST)
			&& prot.getSettingBool(ProtectConfig.protect_signs))
		{
			event.setCancelled(true);
			return;
		}

		final Block block = event.getBlock();
		if (block.getType() == Material.WATER || block.getType() == Material.STATIONARY_WATER)
		{
			event.setCancelled(prot.getSettingBool(ProtectConfig.prevent_water_flow));
			return;
		}

		if (block.getType() == Material.LAVA || block.getType() == Material.STATIONARY_LAVA)
		{
			event.setCancelled(prot.getSettingBool(ProtectConfig.prevent_lava_flow));
			return;
		}

		if (block.getType() == Material.AIR)
		{
			event.setCancelled(prot.getSettingBool(ProtectConfig.prevent_water_bucket_flow));
			return;
		}
	}

	@Override
	public void onBlockBurn(final BlockBurnEvent event)
	{
		final Block block = event.getBlock();
		if (block.getType() == Material.RAILS && prot.getSettingBool(ProtectConfig.protect_rails))
		{
			event.setCancelled(true);
			return;
		}
		if ((block.getType() == Material.WALL_SIGN || block.getType() == Material.SIGN_POST)
			&& prot.getSettingBool(ProtectConfig.protect_signs))
		{
			event.setCancelled(true);
			return;
		}
		if (prot.getSettingBool(ProtectConfig.prevent_fire_spread))
		{
			event.setCancelled(true);
			return;
		}
	}
	private final static BlockFace[] faces = new BlockFace[]
	{
		BlockFace.NORTH,
		BlockFace.EAST,
		BlockFace.SOUTH,
		BlockFace.WEST,
		BlockFace.UP,
		BlockFace.DOWN,
		BlockFace.SELF
	};

	@Override
	public void onBlockBreak(final BlockBreakEvent event)
	{
		if (event.isCancelled())
		{
			return;
		}
		final User user = ess.getUser(event.getPlayer());

		if (prot.getSettingBool(ProtectConfig.disable_build) && !user.canBuild())
		{
			event.setCancelled(true);
			return;
		}
		final Block block = event.getBlock();
		final int typeId = block.getTypeId();

		if (prot.checkProtectionItems(ProtectConfig.blacklist_break, typeId)
			&& !user.isAuthorized("essentials.protect.exemptbreak"))
		{
			event.setCancelled(true);
			return;
		}
		final Material type = block.getType();

		if (prot.checkProtectionItems(ProtectConfig.alert_on_break, typeId))
		{
			prot.alert(user, type.toString(), Util.i18n("alertBroke"));
		}
		final IProtectedBlock storage = prot.getStorage();

		if (user.isAuthorized("essentials.protect.admin"))
		{
			if (type == Material.WALL_SIGN || type == Material.SIGN_POST || type == Material.RAILS)
			{
				storage.unprotectBlock(block);
				if (type == Material.RAILS || type == Material.SIGN_POST)
				{
					final Block below = block.getFace(BlockFace.DOWN);
					storage.unprotectBlock(below);
				}
				else
				{
					for (BlockFace blockFace : faces)
					{
						final Block against = block.getFace(blockFace);
						storage.unprotectBlock(against);
					}
				}
			}
			else
			{
				for (BlockFace blockFace : faces)
				{
					final Block against = block.getFace(blockFace);
					storage.unprotectBlock(against);
				}
			}
		}
		else
		{

			final boolean isProtected = storage.isProtected(block, user.getName());
			if (isProtected)
			{
				event.setCancelled(true);
			}
			else
			{
				if (type == Material.WALL_SIGN || type == Material.SIGN_POST || type == Material.RAILS)
				{
					storage.unprotectBlock(block);
					if (type == Material.RAILS || type == Material.SIGN_POST)
					{
						final Block below = block.getFace(BlockFace.DOWN);
						storage.unprotectBlock(below);
					}
					else
					{
						for (BlockFace blockFace : faces)
						{
							final Block against = block.getFace(blockFace);
							storage.unprotectBlock(against);
						}
					}
				}
				else
				{
					for (BlockFace blockFace : faces)
					{
						final Block against = block.getFace(blockFace);
						storage.unprotectBlock(against);
					}
				}
			}
		}
	}
}
