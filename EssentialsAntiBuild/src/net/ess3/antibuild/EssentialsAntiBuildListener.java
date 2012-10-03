package net.ess3.antibuild;

import java.util.logging.Level;
import static net.ess3.I18n._;
import net.ess3.api.IEssentials;
import net.ess3.api.IUser;
import net.ess3.user.User;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.painting.PaintingBreakByEntityEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;


public class EssentialsAntiBuildListener implements Listener
{
	final private transient IAntiBuild antib;
	final private transient IEssentials ess;

	public EssentialsAntiBuildListener(final IAntiBuild parent)
	{
		this.antib = parent;
		this.ess = antib.getEssentialsConnect().getEssentials();
	}

/*	private boolean metaPermCheck(final User user, final String action, final Block block)
	{
		if (block == null)
		{
			return false;
		}
		return metaPermCheck(user, action, block.getTypeId(), block.getData());
	}

	private boolean metaPermCheck(final User user, final String action, final int blockId)
	{
		final String blockPerm = "essentials.build." + action + "." + blockId;
		return user.isAuthorized(blockPerm);
	}

	private boolean metaPermCheck(final User user, final String action, final int blockId, final byte data)
	{
		final String blockPerm = "essentials.build." + action + "." + blockId;
		final String dataPerm = blockPerm + ":" + data;

		if (user.isPermissionSet(dataPerm))
		{
			return user.isAuthorized(dataPerm);
		}
		else
		{
			if (ess.getSettings().isDebug())
			{
				ess.getLogger().log(Level.INFO, "DataValue perm on " + user.getName() + " is not directly set: " + dataPerm);
			}
		}

		return user.isAuthorized(blockPerm);
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onBlockPlace(final BlockPlaceEvent event)
	{
		final AntiBuildHolder ab = antib.getSettings();
		ab.acquireReadLock();
		final IUser user = ess.getUserMap().getUser(event.getPlayer());
		final Block block = event.getBlockPlaced();
		final int typeId = block.getTypeId();
		final Material type = block.getType();

		try
		{

			if (ab.getData().isBuild()
				&& !user.canBuild() && !user.hasPermission("essentials.build")
				&& !Permissions.getPlacePermission(type).isAuthorized(user))
			//metaPermCheck(user, "place", block)) todo - double check metadata
			{
				if (ab.getData().isWarnOnBuildDisallow())
				{
					user.sendMessage(_("antiBuildPlace", type.toString()));
				}
				event.setCancelled(true);
				return;
			}

			if (ab.getData().getBlacklist().getPlacement().contains(type) && !Permissions.BLACKLIST_ALLOWPLACEMENT.isAuthorized(user))
			//antib.checkProtectionItems(AntiBuildConfig.blacklist_placement, typeId) && !user.isAuthorized("essentials.protect.exemptplacement"))
			{
				if (ab.getData().isWarnOnBuildDisallow())
				{
					user.sendMessage(_("antiBuildPlace", type.toString()));
				}
				event.setCancelled(true);
				return;
			}

			if (ab.getData().getAlert().getAlertOnPlacement().contains(type)
				&& !Permissions.ALERTS_NOTRIGGER.isAuthorized(user))
			{
				antib.getEssentialsConnect().alert(user, type.toString(), _("alertPlaced"));
			}
		}
		finally
		{
			ab.unlock();
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onBlockBreak(final BlockBreakEvent event)
	{
		final AntiBuildHolder ab = antib.getSettings();
		ab.acquireReadLock();
		final IUser user = ess.getUserMap().getUser(event.getPlayer());
		final Block block = event.getBlock();
		final int typeId = block.getTypeId();
		final Material type = block.getType();

		try
		{
			if (ab.getData().isBuild() && !user.canBuild() && !user.isAuthorized("essentials.build")
				&& !Permissions.getBreakPermission(type).isAuthorized(user))
				//!metaPermCheck(user, "break", block))
			{
				if (ab.getData().isWarnOnBuildDisallow())
				{
					user.sendMessage(_("antiBuildBreak", type.toString()));
				}
				event.setCancelled(true);
				return;
			}

			if (ab.getData().getBlacklist().getBreaking().contains(type) && !Permissions.BLACKLIST_ALLOWBREAK.isAuthorized(user))	
			{
				if (ab.getData().isWarnOnBuildDisallow())
				{
					user.sendMessage(_("antiBuildBreak", type.toString()));
				}
				event.setCancelled(true);
				return;
			}

			if (antib.checkProtectionItems(AntiBuildConfig.alert_on_break, typeId)
				&& !user.isAuthorized("essentials.protect.alerts.notrigger"))
			{
				antib.getEssentialsConnect().alert(user, type.toString(), _("alertBroke"));
			}
		}
		finally
		{
			ab.unlock();
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onPaintingBreak(final PaintingBreakByEntityEvent event)
	{
		final Entity entity = event.getRemover();
		if (entity instanceof Player)
		{
			final User user = ess.getUser(entity);
			if (antib.getSettingBool(AntiBuildConfig.disable_build) && !user.canBuild() && !user.isAuthorized("essentials.build")
				&& !metaPermCheck(user, "break", Material.PAINTING.getId()))
			{
				if (ess.getSettings().warnOnBuildDisallow())
				{
					user.sendMessage(_("antiBuildBreak", Material.PAINTING.toString()));
				}
				event.setCancelled(true);
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onBlockPistonExtend(final BlockPistonExtendEvent event)
	{
		for (Block block : event.getBlocks())
		{
			if (antib.checkProtectionItems(AntiBuildConfig.blacklist_piston, block.getTypeId()))
			{
				event.setCancelled(true);
				return;
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onBlockPistonRetract(final BlockPistonRetractEvent event)
	{
		if (!event.isSticky())
		{
			return;
		}
		final Block block = event.getRetractLocation().getBlock();
		if (antib.checkProtectionItems(AntiBuildConfig.blacklist_piston, block.getTypeId()))
		{
			event.setCancelled(true);
			return;
		}
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerInteract(final PlayerInteractEvent event)
	{
		// Do not return if cancelled, because the interact event has 2 cancelled states.
		final User user = ess.getUser(event.getPlayer());
		final ItemStack item = event.getItem();

		if (item != null
			&& antib.checkProtectionItems(AntiBuildConfig.blacklist_usage, item.getTypeId())
			&& !user.isAuthorized("essentials.protect.exemptusage"))
		{
			if (ess.getSettings().warnOnBuildDisallow())
			{
				user.sendMessage(_("antiBuildUse", item.getType().toString()));
			}
			event.setCancelled(true);
			return;
		}

		if (item != null
			&& antib.checkProtectionItems(AntiBuildConfig.alert_on_use, item.getTypeId())
			&& !user.isAuthorized("essentials.protect.alerts.notrigger"))
		{
			antib.getEssentialsConnect().alert(user, item.getType().toString(), _("alertUsed"));
		}

		if (antib.getSettingBool(AntiBuildConfig.disable_use) && !user.canBuild() && !user.isAuthorized("essentials.build"))
		{
			if (event.hasItem() && !metaPermCheck(user, "interact", item.getTypeId(), item.getData().getData()))
			{
				event.setCancelled(true);
				if (ess.getSettings().warnOnBuildDisallow())
				{
					user.sendMessage(_("antiBuildUse", item.getType().toString()));
				}
				return;
			}
			if (event.hasBlock() && !metaPermCheck(user, "interact", event.getClickedBlock()))
			{
				event.setCancelled(true);
				if (ess.getSettings().warnOnBuildDisallow())
				{
					user.sendMessage(_("antiBuildInteract", event.getClickedBlock().getType().toString()));
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void onCraftItemEvent(final CraftItemEvent event)
	{
		HumanEntity entity = event.getWhoClicked();

		if (entity instanceof Player)
		{
			final User user = ess.getUser(entity);
			final ItemStack item = event.getRecipe().getResult();

			if (antib.getSettingBool(AntiBuildConfig.disable_use) && !user.canBuild() && !user.isAuthorized("essentials.build"))
			{
				if (!metaPermCheck(user, "craft", item.getTypeId(), item.getData().getData()))
				{
					event.setCancelled(true);
					if (ess.getSettings().warnOnBuildDisallow())
					{
						user.sendMessage(_("antiBuildCraft", item.getType().toString()));
					}
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void onPlayerPickupItem(PlayerPickupItemEvent event)
	{

		final User user = ess.getUser(event.getPlayer());
		final ItemStack item = event.getItem().getItemStack();

		if (antib.getSettingBool(AntiBuildConfig.disable_use) && !user.canBuild() && !user.isAuthorized("essentials.build"))
		{
			if (!metaPermCheck(user, "pickup", item.getTypeId(), item.getData().getData()))
			{
				event.setCancelled(true);
				event.getItem().setPickupDelay(50);
			}
		}
	}

	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void onPlayerDropItem(final PlayerDropItemEvent event)
	{

		final IUser user = ess.getUserMap().getUser(event.getPlayer());
		final ItemStack item = event.getItemDrop().getItemStack();

		if (antib.getSettingBool(AntiBuildConfig.disable_use) && !user.canBuild() && !user.isAuthorized("essentials.build"))
		{
			if (!metaPermCheck(user, "drop", item.getTypeId(), item.getData().getData()))
			{
				event.setCancelled(true);
				user.getPlayer().updateInventory();
				if (ess.getSettings().warnOnBuildDisallow())
				{
					user.sendMessage(_("antiBuildDrop", item.getType().toString()));
				}
			}
		}
	}
 */
}
