package net.ess3.protect;

import net.ess3.settings.protect.Prevent;
import org.bukkit.entity.*;
import org.bukkit.entity.minecart.ExplosiveMinecart;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityTargetEvent.TargetReason;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingBreakEvent;


public class EssentialsProtectEntityListener implements Listener
{
	private final IProtect prot;

	public EssentialsProtectEntityListener(final IProtect prot)
	{
		super();
		this.prot = prot;
	}

	private Prevent getSettings()
	{
		return prot.getSettings().getData().getPrevent();
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onEntityDamage(final EntityDamageEvent event)
	{
		final Entity target = event.getEntity();

		if (target instanceof Villager && getSettings().isVillagerDeath())
		{
			event.setCancelled(true);
			return;
		}

		final Player user = target instanceof Player ? (Player)target : null;
		if (target instanceof Player && event instanceof EntityDamageByBlockEvent)
		{
			final DamageCause cause = event.getCause();

			if (cause == DamageCause.CONTACT && (Permissions.PREVENTDAMAGE_CONTACT.isAuthorized(user) && !Permissions.PREVENTDAMAGE_NONE.isAuthorized(user)))
			{
				event.setCancelled(true);
				return;
			}
			if (cause == DamageCause.LAVA && (Permissions.PREVENTDAMAGE_LAVADAMAGE.isAuthorized(user) && !Permissions.PREVENTDAMAGE_NONE.isAuthorized(user)))
			{
				event.setCancelled(true);
				return;
			}
			if (cause == DamageCause.BLOCK_EXPLOSION && (Permissions.PREVENTDAMAGE_TNT.isAuthorized(user) && !Permissions.PREVENTDAMAGE_NONE.isAuthorized(user)))
			{
				event.setCancelled(true);
				return;
			}
		}

		if (target instanceof Player && event instanceof EntityDamageByEntityEvent)
		{
			final EntityDamageByEntityEvent edEvent = (EntityDamageByEntityEvent)event;
			final Entity eAttack = edEvent.getDamager();
			final Player attacker = eAttack instanceof Player ? (Player)eAttack : null;

			// PVP Settings
			if (target instanceof Player && eAttack instanceof Player && (!Permissions.PVP.isAuthorized(user) || !Permissions.PVP.isAuthorized(attacker)))
			{
				event.setCancelled(true);
				return;
			}

			//Player damage prevention section
			if (eAttack instanceof Creeper && getSettings().isCreeperPlayerdamage() || (Permissions.PREVENTDAMAGE_CREEPER.isAuthorized(
																						user) && !Permissions.PREVENTDAMAGE_NONE.isAuthorized(user)))
			{
				event.setCancelled(true);
				return;
			}

			if (eAttack instanceof ExplosiveMinecart && getSettings().isTntMinecartPlayerdamage() || (Permissions.PREVENTDAMAGE_TNTMINECART.isAuthorized(
																									  user) && !Permissions.PREVENTDAMAGE_NONE.isAuthorized(user)))
			{
				event.setCancelled(true);
				return;
			}

			if ((event.getEntity() instanceof Fireball || event.getEntity() instanceof SmallFireball || event.getEntity() instanceof LargeFireball) && (Permissions.PREVENTDAMAGE_FIREBALL.isAuthorized(
																																						user) && !Permissions.PREVENTDAMAGE_NONE.isAuthorized(user)))
			{
				event.setCancelled(true);
				return;
			}

			if ((eAttack instanceof WitherSkull && Permissions.PREVENTDAMAGE_WITHERSKULL.isAuthorized(
				 user) && !Permissions.PREVENTDAMAGE_NONE.isAuthorized(user)))
			{
				event.setCancelled(true);
				return;
			}

			if ((eAttack instanceof Wither && event.getCause() == DamageCause.ENTITY_EXPLOSION) && getSettings().isWitherSpawnPlayerdamage() || (Permissions.PREVENTDAMAGE_WITHER.isAuthorized(
																																				 user) && !Permissions.PREVENTDAMAGE_NONE.isAuthorized(user)))
			{
				event.setCancelled(true);
				return;
			}

			if (eAttack instanceof TNTPrimed && (Permissions.PREVENTDAMAGE_TNT.isAuthorized(user) && !Permissions.PREVENTDAMAGE_NONE.isAuthorized(user)))
			{
				event.setCancelled(true);
				return;
			}

			if (edEvent.getDamager() instanceof Projectile && ((Permissions.PREVENTDAMAGE_PROJECTILES.isAuthorized(
																user) && !Permissions.PREVENTDAMAGE_NONE.isAuthorized(
																user)) || (((Projectile)edEvent.getDamager()).getShooter() instanceof Player && (!Permissions.PVP.isAuthorized(
																																				 user) || !Permissions.PVP.isAuthorized((Player)((Projectile)edEvent.getDamager()).getShooter())))))
			{
				event.setCancelled(true);
				return;
			}
		}

		final DamageCause cause = event.getCause();
		if (target instanceof Player)
		{
			if (cause == DamageCause.FALL && (Permissions.PREVENTDAMAGE_FALL.isAuthorized(user) && !Permissions.PREVENTDAMAGE_NONE.isAuthorized(user)))
			{
				event.setCancelled(true);
				return;
			}

			if (cause == DamageCause.SUFFOCATION && (Permissions.PREVENTDAMAGE_SUFFOCATION.isAuthorized(user) && !Permissions.PREVENTDAMAGE_NONE.isAuthorized(
													 user)))
			{
				event.setCancelled(true);
				return;
			}
			if ((cause == DamageCause.FIRE || cause == DamageCause.FIRE_TICK) && (Permissions.PREVENTDAMAGE_FIRE.isAuthorized(
																				  user) && !Permissions.PREVENTDAMAGE_NONE.isAuthorized(user)))
			{
				event.setCancelled(true);
				return;
			}
			if (cause == DamageCause.DROWNING && (Permissions.PREVENTDAMAGE_DROWNING.isAuthorized(user) && !Permissions.PREVENTDAMAGE_NONE.isAuthorized(user)))
			{
				event.setCancelled(true);
				return;
			}
			if (cause == DamageCause.LIGHTNING && (Permissions.PREVENTDAMAGE_LIGHTNING.isAuthorized(user) && !Permissions.PREVENTDAMAGE_NONE.isAuthorized(
												   user)))
			{
				event.setCancelled(true);
			}
			if (cause == DamageCause.WITHER && (Permissions.PREVENTDAMAGE_WITHER.isAuthorized(user)) && !Permissions.PREVENTDAMAGE_NONE.isAuthorized(user))
			{
				event.setCancelled(true);
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onEntityExplode(final EntityExplodeEvent event)
	{
		final int maxHeight = prot.getSettings().getData().getCreeperMaxHeight();
		Entity entity = event.getEntity();

		//Block damage prevention section
		if (entity instanceof EnderDragon && getSettings().isEnderdragonBlockdamage())
		{
			event.setCancelled(true);
		}
		else if (entity instanceof Wither && getSettings().isWitherSpawnBlockdamage())
		{
			event.setCancelled(true);
		}
		else if (entity instanceof Creeper && (getSettings().isCreeperBlockdamage() || getSettings().isCreeperBlockdamage()
											   || (maxHeight >= 0 && event.getLocation().getBlockY() > maxHeight)))
		{
			event.setCancelled(true);
			event.getLocation().getWorld().createExplosion(event.getLocation(), 0F);
		}
		else if (entity instanceof TNTPrimed && getSettings().isTntBlockdamage())
		{
			event.setCancelled(true);
		}
		else if ((entity instanceof Fireball || entity instanceof SmallFireball) && getSettings().isFireballBlockdamage())
		{
			event.setCancelled(true);
		}
		else if ((entity instanceof WitherSkull) && getSettings().isWitherskullBlockdamage())
		{
			event.setCancelled(true);
		}
		else if ((entity instanceof ExplosiveMinecart) && getSettings().isTntMinecartBlockdamage())
		{
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onEntityTarget(final EntityTargetEvent event)
	{
		final Entity entity = event.getTarget();
		if (entity == null)
		{
			return;
		}

		if (entity.getType() == EntityType.PLAYER)
		{
			final Player user = (Player)event.getTarget();
			if ((event.getReason() == TargetReason.CLOSEST_PLAYER || event.getReason() == TargetReason.TARGET_ATTACKED_ENTITY || event.getReason() == TargetReason.PIG_ZOMBIE_TARGET
				 || event.getReason() == TargetReason.RANDOM_TARGET || event.getReason() == TargetReason.TARGET_ATTACKED_OWNER
				 || event.getReason() == TargetReason.OWNER_ATTACKED_TARGET)
				&& !prot.getSettings().getData().getPrevent().isEntitytarget() && !Permissions.ENTITY_TARGET_BYPASS.isAuthorized(
					user, event.getEntity().getType().getName().toLowerCase()))
			{
				event.setCancelled(true);
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onExplosionPrime(final ExplosionPrimeEvent event)
	{
		if ((event.getEntity() instanceof Fireball || event.getEntity() instanceof SmallFireball) && getSettings().isFireballFire())
		{
			event.setFire(false);
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onEntityChangeBlock(final EntityChangeBlockEvent event)
	{
		if (event.getEntityType() == EntityType.ENDERMAN && getSettings().isEndermanPickup())
		{
			event.setCancelled(true);
		}
		if (event.getEntityType() == EntityType.WITHER && getSettings().isWitherBlockreplace())
		{
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onPaintingBreak(final HangingBreakByEntityEvent event)
	{
		final ProtectHolder settings = prot.getSettings();
		Entity remover = event.getRemover();
		if ((event.getCause() == HangingBreakEvent.RemoveCause.ENTITY)
			&& ((remover instanceof Creeper) && getSettings().isCreeperBlockdamage())
			|| ((remover instanceof Wither) && getSettings().isWitherSpawnBlockdamage())
			|| ((remover instanceof Fireball) && getSettings().isFireballBlockdamage())
			|| ((remover instanceof TNTPrimed) && getSettings().isTntBlockdamage())
			|| ((remover instanceof WitherSkull) && getSettings().isWitherskullBlockdamage())
			|| ((remover instanceof ExplosiveMinecart) && getSettings().isTntMinecartBlockdamage()))
		{
			event.setCancelled(true);
		}
	}
}
