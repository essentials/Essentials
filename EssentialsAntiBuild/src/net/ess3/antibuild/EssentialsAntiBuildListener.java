package net.ess3.antibuild;

import java.util.logging.Level;
import static net.ess3.I18n._;
import net.ess3.api.IEssentials;
import net.ess3.api.IUser;
import net.ess3.permissions.MaterialDotStarPermission;
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

	/*private boolean metaPermCheck(final User user, final String action, final Block block)
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
	}*/

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onBlockPlace(final BlockPlaceEvent event)
	{	
		final IUser user = ess.getUserMap().getUser(event.getPlayer());
		final Block block = event.getBlockPlaced();
		final int typeId = block.getTypeId();
		final Material type = block.getType();

		if (antib.getSettings().getData().isDisableBuild()
				//&& !user.canBuild() 
			    && !Permissions.BUILD.isAuthorized(user)
				&& !Permissions.PLACEMENT.isAuthorized(user, block))
			//metaPermCheck(user, "place", block)) todo - double check metadata
			{
				if (antib.getSettings().getData().isWarnOnBuildDisallow())
				{
					user.sendMessage(_("antiBuildPlace", type.toString()));
				}
				event.setCancelled(true);
				return;
			}

			if (antib.getSettings().getData().getBlacklist().getPlacement().contains(type) && !Permissions.BLACKLIST_ALLOWPLACEMENT.isAuthorized(user))			
			{
				if (antib.getSettings().getData().isWarnOnBuildDisallow())
				{
					user.sendMessage(_("antiBuildPlace", type.toString()));
				}
				event.setCancelled(true);
				return;
			}

			if (antib.getSettings().getData().getAlert().getAlertOnPlacement().contains(type)
				&& !Permissions.ALERTS_NOTRIGGER.isAuthorized(user))
			{
				antib.getEssentialsConnect().alert(user, type.toString(), _("alertPlaced"));
			}				
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onBlockBreak(final BlockBreakEvent event)
	{
		final IUser user = ess.getUserMap().getUser(event.getPlayer());
		final Block block = event.getBlock();
		final int typeId = block.getTypeId();
		final Material type = block.getType();
		
			if (antib.getSettings().getData().isDisableBuild()
				//&& !user.canBuild() 
				&& !Permissions.BUILD.isAuthorized(user)
				&& !Permissions.BREAK.isAuthorized(user, block))
			{
				if (antib.getSettings().getData().isWarnOnBuildDisallow())
				{
					user.sendMessage(_("antiBuildBreak", type.toString()));
				}
				event.setCancelled(true);
				return;
			}

			if (antib.getSettings().getData().getBlacklist().getBreaking().contains(type) && !Permissions.BLACKLIST_ALLOWBREAK.isAuthorized(user))	
			{
				if (antib.getSettings().getData().isWarnOnBuildDisallow())
				{
					user.sendMessage(_("antiBuildBreak", type.toString()));
				}
				event.setCancelled(true);
				return;
			}

			if (antib.getSettings().getData().getAlert().getAlertOnBreak().contains(type)
				&& !Permissions.ALERTS_NOTRIGGER.isAuthorized(user))
			{
				antib.getEssentialsConnect().alert(user, type.toString(), _("alertBroke"));
			}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onPaintingBreak(final PaintingBreakByEntityEvent event)
	{
		final Entity entity = event.getRemover();
		if (entity instanceof Player)
		{
			final IUser user = ess.getUserMap().getUser((Player)entity);
			if (antib.getSettings().getData().isDisableBuild() 
				//&& !user.canBuild() 
				&& !Permissions.BUILD.isAuthorized(user)
				&& !Permissions.BREAK.isAuthorized(user, Material.PAINTING, null))
			{
				if (antib.getSettings().getData().isWarnOnBuildDisallow())
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
			if (antib.getSettings().getData().getBlacklist().getPiston().contains(block.getType()))
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
		if (antib.getSettings().getData().getBlacklist().getPiston().contains(block.getType()))
		{
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerInteract(final PlayerInteractEvent event)
	{
		// Do not return if cancelled, because the interact event has 2 cancelled states.
		final IUser user = ess.getUserMap().getUser(event.getPlayer());
		final ItemStack item = event.getItem();

		if (item != null
			&& antib.getSettings().getData().getBlacklist().getUsage().contains(item.getType())
			&& !Permissions.BLACKLIST_ALLOWUSAGE.isAuthorized(user))
		{
			if (antib.getSettings().getData().isWarnOnBuildDisallow())
			{
				user.sendMessage(_("antiBuildUse", item.getType().toString()));
			}
			event.setCancelled(true);
			return;
		}

		if (item != null
			&& antib.getSettings().getData().getAlert().getAlertOnUse().contains(item.getType())
			&& !Permissions.ALERTS_NOTRIGGER.isAuthorized(user))
		{
			antib.getEssentialsConnect().alert(user, item.getType().toString(), _("alertUsed"));
		}

		if (antib.getSettings().getData().isDisableUse()
			//&& !user.canBuild() 
			&& !Permissions.BUILD.isAuthorized(user))
		{
			if (event.hasItem() && !Permissions.INTERACT.isAuthorized(user, item.getType(), item.getData()))
			{
				event.setCancelled(true);
				if (antib.getSettings().getData().isWarnOnBuildDisallow())
				{
					user.sendMessage(_("antiBuildUse", item.getType().toString()));
				}
				return;
			}
			if (event.hasBlock() && !Permissions.INTERACT.isAuthorized(user, event.getClickedBlock()))
			{
				event.setCancelled(true);
				if (antib.getSettings().getData().isWarnOnBuildDisallow())
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
			final IUser user = ess.getUserMap().getUser((Player)entity);
			final ItemStack item = event.getRecipe().getResult();

			if (antib.getSettings().getData().isDisableUse() 
				//&& !user.canBuild() 
				&& !Permissions.BUILD.isAuthorized(user))
			{
				if (!Permissions.CRAFT.isAuthorized(user, item.getType(), item.getData()))
				{
					event.setCancelled(true);
					if (antib.getSettings().getData().isWarnOnBuildDisallow())
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

		final IUser user = ess.getUserMap().getUser(event.getPlayer());
		final ItemStack item = event.getItem().getItemStack();

		if (antib.getSettings().getData().isDisableUse() 			
			//&& !user.canBuild() 
			&& !Permissions.BUILD.isAuthorized(user))
		{
			if (!Permissions.PICKUP.isAuthorized(user, item.getType(), item.getData()))
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

		if (antib.getSettings().getData().isDisableUse()
			//&& !user.canBuild() 
			&& !Permissions.BUILD.isAuthorized(user));
		{
			if (!Permissions.DROP.isAuthorized(user, item.getType(), item.getData()))
			{
				event.setCancelled(true);
				user.getPlayer().updateInventory();
				if (antib.getSettings().getData().isWarnOnBuildDisallow())
				{
					user.sendMessage(_("antiBuildDrop", item.getType().toString()));
				}
			}
		}
	} 
}
