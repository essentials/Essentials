package com.earth2me.essentials.listener;

import static com.earth2me.essentials.I18n._;
import com.earth2me.essentials.api.IEssentials;
import com.earth2me.essentials.api.ISettings;
import com.earth2me.essentials.api.IUser;
import com.earth2me.essentials.permissions.Permissions;
import java.util.List;
import lombok.Cleanup;
import org.bukkit.Material;
import org.bukkit.entity.Animals;
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

	@EventHandler(priority = EventPriority.LOWEST)
	public void onEntityDamage(final EntityDamageByEntityEvent event)
	{
		final Entity eAttack = event.getDamager();
		final Entity eDefend = event.getEntity();
		if (eDefend instanceof Player && eAttack instanceof Player)
		{
			@Cleanup
			final IUser attacker = ess.getUser((Player)eAttack);
			attacker.acquireReadLock();
			attacker.updateActivity(true);
			final ItemStack itemstack = attacker.getItemInHand();
			final List<String> commandList = attacker.getData().getPowertool(itemstack.getType());
			if (commandList != null && !commandList.isEmpty())
			{
				for (String command : commandList)
				{
					if (command != null && !command.isEmpty())
					{
						attacker.getServer().dispatchCommand(attacker, command.replaceAll("\\{player\\}", ((Player)eDefend).getName()));
						event.setCancelled(true);
						return;
					}
				}
			}
		}
		else if (eDefend instanceof Animals && eAttack instanceof Player)
		{
			final Player player = (Player)eAttack;
			final ItemStack hand = player.getItemInHand();
			if (hand != null && hand.getType() == Material.MILK_BUCKET)
			{
				((Animals)eDefend).setBaby();
				hand.setType(Material.BUCKET);
				player.setItemInHand(hand);
				player.updateInventory();
				event.setCancelled(true);
			}
		}
	}

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
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

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
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

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onFoodLevelChange(final FoodLevelChangeEvent event)
	{
		if (event.getEntity() instanceof Player && ess.getUser((Player)event.getEntity()).isGodModeEnabled())
		{
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
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
