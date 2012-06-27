package net.ess3.listener;

import static net.ess3.I18n._;
import net.ess3.api.IEssentials;
import net.ess3.api.ISettings;
import net.ess3.api.IUser;
import net.ess3.permissions.Permissions;
import net.ess3.user.UserData.TimestampType;
import java.util.List;
import lombok.Cleanup;
import org.bukkit.Material;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;
import org.bukkit.event.entity.*;
import org.bukkit.inventory.ItemStack;


public class EssentialsEntityListener implements Listener
{
	private final transient IEssentials ess;

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
			@Cleanup
			final IUser attacker = ess.getUser((Player)eAttack);
			@Cleanup
			final IUser defender = ess.getUser((Player)eDefend);
			@Cleanup
			ISettings settings = ess.getSettings();
			settings.acquireReadLock();
			attacker.acquireReadLock();
			defender.acquireReadLock();

			attacker.updateActivity(true);
			if (settings.getData().getGeneral().getLoginAttackDelay() > 0 && !Permissions.PVPDELAY_EXEMPT.isAuthorized(attacker)
				&& (System.currentTimeMillis() < (attacker.getTimestamp(TimestampType.LOGIN) + settings.getData().getGeneral().getLoginAttackDelay())))
			{
				event.setCancelled(true);
			}
			if (attacker.hasInvulnerabilityAfterTeleport() || defender.hasInvulnerabilityAfterTeleport())
			{
				event.setCancelled(true);
			}
			final ItemStack itemstack = attacker.getItemInHand();
			final List<String> commandList = attacker.getData().getPowertool(itemstack.getType());
			if (commandList != null && !commandList.isEmpty())
			{
				for (final String command : commandList)
				{
					if (command != null && !command.isEmpty())
					{
						ess.scheduleSyncDelayedTask(
								new Runnable()
								{
									@Override
									public void run()
									{
										attacker.getServer().dispatchCommand(attacker.getBase(), command.replaceAll("\\{player\\}", defender.getName()));
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
		if (event.getEntity() instanceof Player && ess.getUser((Player)event.getEntity()).isGodModeEnabled())
		{
			final Player player = (Player)event.getEntity();
			player.setFireTicks(0);
			player.setRemainingAir(player.getMaximumAir());
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void onEntityCombust(final EntityCombustEvent event)
	{
		if (event.getEntity() instanceof Player && ess.getUser((Player)event.getEntity()).isGodModeEnabled())
		{
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerDeathEvent(final PlayerDeathEvent event)
	{
		final IUser user = ess.getUser((Player)event.getEntity());
		@Cleanup
		final ISettings settings = ess.getSettings();
		settings.acquireReadLock();
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
		final IUser user = ess.getUser(event.getEntity());
		if (Permissions.KEEPXP.isAuthorized(user))
		{
			event.setKeepLevel(true);
			event.setDroppedExp(0);
		}
	}

	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void onFoodLevelChange(final FoodLevelChangeEvent event)
	{
		if (event.getEntity() instanceof Player && ess.getUser((Player)event.getEntity()).isGodModeEnabled())
		{
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void onEntityRegainHealth(final EntityRegainHealthEvent event)
	{

		if (event.getRegainReason() == RegainReason.SATIATED && event.getEntity() instanceof Player)
		{
			@Cleanup
			final ISettings settings = ess.getSettings();
			settings.acquireReadLock();
			@Cleanup
			final IUser user = ess.getUser((Player)event.getEntity());
			user.acquireReadLock();
			if (user.getData().isAfk() && settings.getData().getCommands().getAfk().isFreezeAFKPlayers())
			{
				event.setCancelled(true);
			}
		}
	}
}
