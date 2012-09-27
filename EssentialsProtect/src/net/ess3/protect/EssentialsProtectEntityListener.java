package net.ess3.protect;

import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityTargetEvent.TargetReason;


public class EssentialsProtectEntityListener implements Listener
{
	private final transient IProtect prot;

	public EssentialsProtectEntityListener(final IProtect prot)
	{
		super();
		this.prot = prot;
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onEntityDamage(final EntityDamageEvent event)
	{
		final ProtectHolder settings = prot.getSettings();
		settings.acquireReadLock();
		try
		{
			final Entity target = event.getEntity();

			if (target instanceof Villager && settings.getData().getPrevent().isVillagerDeath())
			{
				event.setCancelled(true);
				return;
			}

			final Player user = target instanceof Player ? (Player)target : null;
			if (target instanceof Player && event instanceof EntityDamageByBlockEvent)
			{
				final DamageCause cause = event.getCause();

				if (cause == DamageCause.CONTACT
					&& (Permissions.PREVENTDAMAGE_CONTACT.isAuthorized(user)
						&& !Permissions.PREVENTDAMAGE_NONE.isAuthorized(user)))
				{
					event.setCancelled(true);
					return;
				}
				if (cause == DamageCause.LAVA
					&& (Permissions.PREVENTDAMAGE_LAVADAMAGE.isAuthorized(user)
						&& !Permissions.PREVENTDAMAGE_NONE.isAuthorized(user)))
				{
					event.setCancelled(true);
					return;
				}
				if (cause == DamageCause.BLOCK_EXPLOSION
					&& (Permissions.PREVENTDAMAGE_TNT.isAuthorized(user)
						&& !Permissions.PREVENTDAMAGE_NONE.isAuthorized(user)))
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
				if (target instanceof Player && eAttack instanceof Player
					&& (!Permissions.PVP.isAuthorized(user) || !Permissions.PVP.isAuthorized(attacker)))
				{
					event.setCancelled(true);
					return;
				}

				//Creeper explode prevention
				if (eAttack instanceof Creeper && settings.getData().getPrevent().isCreeperExplosion()
					|| (Permissions.PREVENTDAMAGE_CREEPER.isAuthorized(user)
						&& !Permissions.PREVENTDAMAGE_NONE.isAuthorized(user)))
				{
					event.setCancelled(true);
					return;
				}

				if ((event.getEntity() instanceof Fireball || event.getEntity() instanceof SmallFireball)
					&& (Permissions.PREVENTDAMAGE_FIREBALL.isAuthorized(user)
						&& !Permissions.PREVENTDAMAGE_NONE.isAuthorized(user)))
				{
					event.setCancelled(true);
					return;
				}

				if (eAttack instanceof TNTPrimed
					&& (Permissions.PREVENTDAMAGE_TNT.isAuthorized(user)
						&& !Permissions.PREVENTDAMAGE_NONE.isAuthorized(user)))
				{
					event.setCancelled(true);
					return;
				}

				if (edEvent.getDamager() instanceof Projectile
					&& ((Permissions.PREVENTDAMAGE_PROJECTILES.isAuthorized(user)
						 && !Permissions.PREVENTDAMAGE_NONE.isAuthorized(user))
						|| (((Projectile)edEvent.getDamager()).getShooter() instanceof Player
							&& (!Permissions.PVP.isAuthorized(user)
								|| !Permissions.PVP.isAuthorized((Player)((Projectile)edEvent.getDamager()).getShooter())))))
				{
					event.setCancelled(true);
					return;
				}
			}

			final DamageCause cause = event.getCause();
			if (target instanceof Player)
			{
				if (cause == DamageCause.FALL
					&& (Permissions.PREVENTDAMAGE_FALL.isAuthorized(user)
						&& !Permissions.PREVENTDAMAGE_NONE.isAuthorized(user)))
				{
					event.setCancelled(true);
					return;
				}

				if (cause == DamageCause.SUFFOCATION
					&& (Permissions.PREVENTDAMAGE_SUFFOCATION.isAuthorized(user)
						&& !Permissions.PREVENTDAMAGE_NONE.isAuthorized(user)))
				{
					event.setCancelled(true);
					return;
				}
				if ((cause == DamageCause.FIRE
					 || cause == DamageCause.FIRE_TICK)
					&& (Permissions.PREVENTDAMAGE_FIRE.isAuthorized(user)
						&& !Permissions.PREVENTDAMAGE_NONE.isAuthorized(user)))
				{
					event.setCancelled(true);
					return;
				}
				if (cause == DamageCause.DROWNING
					&& (Permissions.PREVENTDAMAGE_DROWNING.isAuthorized(user)
						&& !Permissions.PREVENTDAMAGE_NONE.isAuthorized(user)))
				{
					event.setCancelled(true);
					return;
				}
				if (cause == DamageCause.LIGHTNING
					&& (Permissions.PREVENTDAMAGE_LIGHTNING.isAuthorized(user)
						&& !Permissions.PREVENTDAMAGE_NONE.isAuthorized(user)))
				{
					event.setCancelled(true);					
				}
			}
		}
		finally
		{
			settings.unlock();
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onEntityExplode(final EntityExplodeEvent event)
	{
		final ProtectHolder settings = prot.getSettings();
		settings.acquireReadLock();
		try
		{
			final int maxHeight = settings.getData().getCreeperMaxHeight();

			if (event.getEntity() instanceof EnderDragon
				&& settings.getData().getPrevent().isEnderdragonBlockdamage())
			{
				event.setCancelled(true);
				return;
			}
			else if (event.getEntity() instanceof Creeper
					 && (settings.getData().getPrevent().isCreeperExplosion()
						 || settings.getData().getPrevent().isCreeperBlockdamage()
						 || (maxHeight >= 0 && event.getLocation().getBlockY() > maxHeight)))
			{
				event.setCancelled(true);
				event.getLocation().getWorld().createExplosion(event.getLocation(), 0F);
				return;
			}
			else if (event.getEntity() instanceof TNTPrimed
					 && settings.getData().getPrevent().isTntExplosion())
			{
				event.setCancelled(true);
				return;
			}
			else if ((event.getEntity() instanceof Fireball || event.getEntity() instanceof SmallFireball)
					 && settings.getData().getPrevent().isFireballExplosion())
			{
				event.setCancelled(true);
				return;
			}	
		}
		finally
		{
			settings.unlock();
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onEntityTarget(final EntityTargetEvent event)
	{
		if (event.getTarget().getType() == EntityType.PLAYER)
		{
			final Player user = (Player)event.getTarget();
			if ((event.getReason() == TargetReason.CLOSEST_PLAYER
				 || event.getReason() == TargetReason.TARGET_ATTACKED_ENTITY
				 || event.getReason() == TargetReason.PIG_ZOMBIE_TARGET
				 || event.getReason() == TargetReason.RANDOM_TARGET
				 || event.getReason() == TargetReason.TARGET_ATTACKED_OWNER
				 || event.getReason() == TargetReason.OWNER_ATTACKED_TARGET)
				&& Permissions.ENTITYTARGET.isAuthorized(user))
			{
				event.setCancelled(true);
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onExplosionPrime(final ExplosionPrimeEvent event)
	{
		final ProtectHolder settings = prot.getSettings();
		settings.acquireReadLock();
		try
		{
			if ((event.getEntity() instanceof Fireball || event.getEntity() instanceof SmallFireball)
				&& settings.getData().getPrevent().isFireballFire())
			{
				event.setFire(false);
			}
		}
		finally
		{
			settings.unlock();
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onEntityChangeBlock(final EntityChangeBlockEvent event)
	{
		final ProtectHolder settings = prot.getSettings();
		settings.acquireReadLock();
		try
		{
			if (event.getEntityType() == EntityType.ENDERMAN && settings.getData().getPrevent().isEndermanPickup())
			{
				event.setCancelled(true);
			}
		}
		finally
		{
			settings.unlock();
		}
	}
}
