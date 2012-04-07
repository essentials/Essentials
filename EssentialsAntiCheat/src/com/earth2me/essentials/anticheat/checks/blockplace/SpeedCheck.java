package com.earth2me.essentials.anticheat.checks.blockplace;

import com.earth2me.essentials.anticheat.NoCheat;
import com.earth2me.essentials.anticheat.NoCheatPlayer;
import com.earth2me.essentials.anticheat.actions.ParameterName;
import com.earth2me.essentials.anticheat.data.Statistics.Id;
import java.util.Locale;


public class SpeedCheck extends BlockPlaceCheck
{
	public SpeedCheck(NoCheat plugin)
	{
		super(plugin, "blockplace.speed");
	}

	public boolean check(NoCheatPlayer player, BlockPlaceData data, BlockPlaceConfig cc)
	{
		boolean cancel = false;

		if (data.lastPlace != 0 && System.currentTimeMillis() - data.lastPlace < cc.speedTime)
		{
			// He failed, increase vl and statistics
			data.speedVL += cc.speedTime - System.currentTimeMillis() + data.lastPlace;
			incrementStatistics(player, Id.BP_SPEED, cc.speedTime - System.currentTimeMillis() + data.lastPlace);

			// Execute whatever actions are associated with this check and the
			// violation level and find out if we should cancel the event
			cancel = executeActions(player, cc.speedActions, data.speedVL);
		}
		else
		// Reward with lowering of the violation level
		{
			data.speedVL *= 0.90D;
		}

		data.lastPlace = System.currentTimeMillis();

		return cancel;
	}

	@Override
	public String getParameter(final ParameterName wildcard, final NoCheatPlayer player)
	{
		if (wildcard == ParameterName.VIOLATIONS)
		{
			return String.format(Locale.US, "%d", (int)getData(player).speedVL);
		}
		else
		{
			return super.getParameter(wildcard, player);
		}
	}
}
