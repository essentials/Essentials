package com.earth2me.essentials.anticheat.checks.blockbreak;

import java.util.Locale;
import com.earth2me.essentials.anticheat.NoCheat;
import com.earth2me.essentials.anticheat.NoCheatPlayer;
import com.earth2me.essentials.anticheat.actions.ParameterName;
import com.earth2me.essentials.anticheat.data.Statistics.Id;


/**
 * We require that the player moves his arm between blockbreaks, this is what gets checked here.
 *
 */
public class NoswingCheck extends BlockBreakCheck
{
	public NoswingCheck(NoCheat plugin)
	{
		super(plugin, "blockbreak.noswing");
	}

	public boolean check(NoCheatPlayer player, BlockBreakData data, BlockBreakConfig cc)
	{

		boolean cancel = false;

		// did he swing his arm before
		if (data.armswung)
		{
			// "consume" the flag
			data.armswung = false;
			// reward with lowering of the violation level
			data.noswingVL *= 0.90D;
		}
		else
		{
			// he failed, increase vl and statistics
			data.noswingVL += 1;
			incrementStatistics(player, Id.BB_NOSWING, 1);

			// Execute whatever actions are associated with this check and the
			// violation level and find out if we should cancel the event
			cancel = executeActions(player, cc.noswingActions, data.noswingVL);
		}

		return cancel;
	}

	@Override
	public String getParameter(ParameterName wildcard, NoCheatPlayer player)
	{

		if (wildcard == ParameterName.VIOLATIONS)
		{
			return String.format(Locale.US, "%d", (int)getData(player).noswingVL);
		}
		else
		{
			return super.getParameter(wildcard, player);
		}
	}
}
