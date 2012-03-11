package com.earth2me.essentials.anticheat.checks.inventory;

import com.earth2me.essentials.anticheat.NoCheat;
import com.earth2me.essentials.anticheat.NoCheatPlayer;
import com.earth2me.essentials.anticheat.actions.ParameterName;
import com.earth2me.essentials.anticheat.data.Statistics.Id;
import java.util.Locale;
import org.bukkit.event.entity.FoodLevelChangeEvent;


/**
 * The InstantEatCheck will find out if a player eats his food too fast
 */
public class InstantEatCheck extends InventoryCheck
{
	public InstantEatCheck(NoCheat plugin)
	{
		super(plugin, "inventory.instanteat");
	}

	public boolean check(NoCheatPlayer player, FoodLevelChangeEvent event, InventoryData data, InventoryConfig cc)
	{

		// Hunger level change seems to not be the result of eating
		if (data.foodMaterial == null || event.getFoodLevel() <= player.getPlayer().getFoodLevel())
		{
			return false;
		}

		boolean cancelled = false;

		long time = System.currentTimeMillis();
		// rough estimation about how long it should take to eat
		long expectedTimeWhenEatingFinished = data.lastEatInteractTime + 700;

		if (expectedTimeWhenEatingFinished < time)
		{
			// Acceptable, reduce VL to reward the player
			data.instantEatVL *= 0.60D;
		}
		else if (data.lastEatInteractTime > time)
		{
			// Security test, if time ran backwards, reset
			data.lastEatInteractTime = 0;
		}
		else
		{
			// Player was too fast, increase violation level and statistics
			int vl = ((int)(expectedTimeWhenEatingFinished - time)) / 100;
			data.instantEatVL += vl;
			incrementStatistics(player, Id.INV_EAT, vl);

			// Execute whatever actions are associated with this check and the
			// violation level and find out if we should cancel the event
			cancelled = executeActions(player, cc.eatActions, data.instantEatVL);
		}

		return cancelled;
	}

	@Override
	public String getParameter(ParameterName wildcard, NoCheatPlayer player)
	{

		if (wildcard == ParameterName.VIOLATIONS)
		{
			return String.format(Locale.US, "%d", (int)getData(player).instantEatVL);
		}
		else if (wildcard == ParameterName.FOOD)
		{
			return getData(player).foodMaterial.toString();
		}
		else
		{
			return super.getParameter(wildcard, player);
		}
	}
}
