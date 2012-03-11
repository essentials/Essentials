package com.earth2me.essentials.anticheat.checks.inventory;

import com.earth2me.essentials.anticheat.NoCheat;
import com.earth2me.essentials.anticheat.NoCheatPlayer;
import com.earth2me.essentials.anticheat.actions.ParameterName;
import com.earth2me.essentials.anticheat.data.Statistics.Id;
import java.util.Locale;


/**
 * The DropCheck will find out if a player drops too many items within a short amount of time
 *
 */
public class DropCheck extends InventoryCheck
{
	public DropCheck(NoCheat plugin)
	{
		super(plugin, "inventory.drop");
	}

	public boolean check(NoCheatPlayer player, InventoryData data, InventoryConfig cc)
	{

		boolean cancel = false;

		final long time = System.currentTimeMillis();

		// Has the configured time passed? If so, reset the counter
		if (data.dropLastTime + cc.dropTimeFrame <= time)
		{
			data.dropLastTime = time;
			data.dropCount = 0;
			data.dropVL = 0;
		}
		// Security check, if the system time changes
		else if (data.dropLastTime > time)
		{
			data.dropLastTime = Integer.MIN_VALUE;
		}

		data.dropCount++;

		// The player dropped more than he should
		if (data.dropCount > cc.dropLimit)
		{
			// Set vl and increment statistics
			data.dropVL = data.dropCount - cc.dropLimit;
			incrementStatistics(player, Id.INV_DROP, 1);

			// Execute whatever actions are associated with this check and the
			// violation level and find out if we should cancel the event
			cancel = executeActions(player, cc.dropActions, data.dropVL);
		}

		return cancel;
	}

	@Override
	public String getParameter(ParameterName wildcard, NoCheatPlayer player)
	{

		if (wildcard == ParameterName.VIOLATIONS)
		{
			return String.format(Locale.US, "%d", getData(player).dropVL);
		}
		else
		{
			return super.getParameter(wildcard, player);
		}
	}
}
