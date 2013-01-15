package net.ess3.listener;

import java.util.List;
import static net.ess3.I18n._;
import net.ess3.api.IEssentials;
import net.ess3.api.ISettings;
import net.ess3.api.IUser;
import net.ess3.permissions.Permissions;
import net.ess3.user.UserData.TimestampType;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;
import org.bukkit.inventory.ItemStack;


public class EssentialsEntityListener implements Listener
{
	private final IEssentials ess;

	public EssentialsEntityListener(final IEssentials ess)
	{
		this.ess = ess;
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onEntityDamage(final EntityDamageByEntityEvent event)
	{
		final Entity eAttack = event.getDamager();
		final Entity eDefend = event.getEntity();

		if (eDefend instanceof Player && eAttack instanceof Player)
		{
			final IUser attacker = ess.getUserMap().getUser((Player)eAttack);
			final IUser defender = ess.getUserMap().getUser((Player)eDefend);
			ISettings settings = ess.getSettings();

			attacker.updateActivity(true);
			if (settings.getData().getGeneral().getLoginAttackDelay() > 0 && !Permissions.PVPDELAY_EXEMPT.isAuthorized(
					attacker) && (System.currentTimeMillis() < (attacker.getTimestamp(
					TimestampType.LOGIN) + settings.getData().getGeneral().getLoginAttackDelay())))
			{
				event.setCancelled(true);
			}
			if (attacker.hasInvulnerabilityAfterTeleport() || defender.hasInvulnerabilityAfterTeleport())
			{
				event.setCancelled(true);
			}
			if (attacker.isVanished() && !Permissions.VANISH_PVP.isAuthorized(attacker))
			{
				event.setCancelled(true);
			}

			final ItemStack itemstack = ((Player)eAttack).getItemInHand();
			final List<String> commandList = attacker.getData().getPowertool(itemstack.getType());
			if (commandList != null && !commandList.isEmpty())
			{
				for (final String command : commandList)
				{
					if (command != null && !command.isEmpty())
					{
						ess.getPlugin().scheduleSyncDelayedTask(
								new Runnable()
								{
									@Override
									public void run()
									{
										attacker.getServer().dispatchCommand(((Player)eAttack), command.replaceAll("\\{player\\}", defender.getName()));
									}
								});
						event.setCancelled(true);
						return;
					}
				}
			}
		}
		else if (eDefend instanceof Ageable && eAttack instanceof Player)
		{
			final Player player = (Player)eAttack;
			final ItemStack hand = player.getItemInHand();
			if (hand != null && hand.getType() == Material.MILK_BUCKET)
			{
				((Ageable)eDefend).setBaby();
				hand.setType(Material.BUCKET);
				player.setItemInHand(hand);
				player.updateInventory();
				event.setCancelled(true);
			}
		}
	}

	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void onEntityDamage(final EntityDamageEvent event)
	{
		final Entity entity = event.getEntity();
		if (entity instanceof Player)
		{
			final Player player = (Player)entity;
			if (ess.getUserMap().getUser(player).isGodModeEnabled())
			{
				player.setFireTicks(0);
				player.setRemainingAir(player.getMaximumAir());
				event.setCancelled(true);
			}
		}
	}

	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void onEntityCombust(final EntityCombustEvent event)
	{
		final Entity entity = event.getEntity();
		if (entity instanceof Player && ess.getUserMap().getUser((Player)entity).isGodModeEnabled())
		{
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerDeathEvent(final PlayerDeathEvent event)
	{
		final IUser user = ess.getUserMap().getUser((Player)event.getEntity());

		final ISettings settings = ess.getSettings();
		if (Permissions.BACK_ONDEATH.isAuthorized(user) && !settings.getData().getCommands().isDisabled("back"))
		{
			user.setLastLocation();
			user.sendMessage(_("backAfterDeath"));
		}
		if (!settings.getData().getGeneral().isDeathMessages())
		{
			event.setDeathMessage("");
		}
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerDeathExpEvent(final PlayerDeathEvent event)
	{
		final IUser user = ess.getUserMap().getUser(event.getEntity());
		if (Permissions.KEEPXP.isAuthorized(user))
		{
			event.setKeepLevel(true);
			event.setDroppedExp(0);
		}
	}

	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void onFoodLevelChange(final FoodLevelChangeEvent event)
	{
		final Entity entity = event.getEntity();
		if (entity instanceof Player && ess.getUserMap().getUser((Player)entity).isGodModeEnabled())
		{
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void onEntityRegainHealth(final EntityRegainHealthEvent event)
	{

		if (event.getRegainReason() == RegainReason.SATIATED && event.getEntity() instanceof Player)
		{

			final ISettings settings = ess.getSettings();

			final IUser user = ess.getUserMap().getUser((Player)event.getEntity());
			if (user.getData().isAfk() && settings.getData().getCommands().getAfk().isFreezeAFKPlayers())
			{
				event.setCancelled(true);
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onCreatureSpawn(final CreatureSpawnEvent event)
	{
		if (event.getEntity().getType() == EntityType.PLAYER)
		{
			return;
		}
		final EntityType creature = event.getEntityType();
		if (creature == null)
		{
			return;
		}
		final ISettings settings = ess.getSettings();
		final Boolean prevent = settings.getData().getWorldOptions(event.getLocation().getWorld().getName()).getPreventSpawn(creature);
		if (prevent != null && prevent)
		{
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void onPotionSplashEvent(final PotionSplashEvent event)
	{
		for (LivingEntity entity : event.getAffectedEntities())
		{
			if (entity instanceof Player)
			{
				final IUser user = ess.getUserMap().getUser((Player)entity);
				if (user.isGodModeEnabled())
				{
					event.setIntensity(entity, 0d);
				}
			}
		}
	}
}
