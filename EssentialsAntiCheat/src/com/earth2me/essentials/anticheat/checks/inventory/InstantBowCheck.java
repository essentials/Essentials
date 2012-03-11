package com.earth2me.essentials.anticheat.checks.inventory;

import com.earth2me.essentials.anticheat.NoCheat;
import com.earth2me.essentials.anticheat.NoCheatPlayer;
import com.earth2me.essentials.anticheat.actions.ParameterName;
import com.earth2me.essentials.anticheat.data.Statistics.Id;
import java.util.Locale;
import org.bukkit.event.entity.EntityShootBowEvent;


/**
 * The InstantBowCheck will find out if a player pulled the string of his bow too fast
 */
public class InstantBowCheck extends InventoryCheck
{
	public InstantBowCheck(NoCheat plugin)
	{
		super(plugin, "inventory.instantbow");
	}

	public boolean check(NoCheatPlayer player, EntityShootBowEvent event, InventoryData data, InventoryConfig cc)
	{

		boolean cancelled = false;

		long time = System.currentTimeMillis();

		// How fast will the arrow be?
		float bowForce = event.getForce();

		// Rough estimation of how long pulling the string should've taken
		long expectedTimeWhenStringDrawn = data.lastBowInteractTime + (int)(bowForce * bowForce * 700F);

		if (expectedTimeWhenStringDrawn < time)
		{
			// The player was slow enough, reward him by lowering the vl
			data.instantBowVL *= 0.90D;
		}
		else if (data.lastBowInteractTime > time)
		{
			// Security check if time ran backwards, reset
			data.lastBowInteractTime = 0;
		}
		else
		{
			// Player was too fast, increase violation level and statistics
			int vl = ((int)(expectedTimeWhenStringDrawn - time)) / 100;
			data.instantBowVL += vl;
			incrementStatistics(player, Id.INV_BOW, vl);

			// Execute whatever actions are associated with this check and the
			// violation level and find out if we should cancel the event
			cancelled = executeActions(player, cc.bowActions, data.instantBowVL);
		}

		return cancelled;
	}

	@Override
	public String getParameter(ParameterName wildcard, NoCheatPlayer player)
	{

		if (wildcard == ParameterName.VIOLATIONS)
		{
			return String.format(Locale.US, "%d", getData(player).instantBowVL);
		}
		else
		{
			return super.getParameter(wildcard, player);
		}
	}
}
