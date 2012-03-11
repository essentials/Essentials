package com.earth2me.essentials.anticheat.checks.fight;

import com.earth2me.essentials.anticheat.NoCheat;
import com.earth2me.essentials.anticheat.NoCheatPlayer;
import com.earth2me.essentials.anticheat.actions.ParameterName;
import com.earth2me.essentials.anticheat.config.Permissions;
import com.earth2me.essentials.anticheat.data.Statistics.Id;
import java.util.Locale;


/**
 * We require that the player moves his arm between attacks, this is what gets checked here.
 *
 */
public class NoswingCheck extends FightCheck
{
	public NoswingCheck(NoCheat plugin)
	{
		super(plugin, "fight.noswing", Permissions.FIGHT_NOSWING);
	}

	public boolean check(NoCheatPlayer player, FightData data, FightConfig cc)
	{

		boolean cancel = false;

		// did he swing his arm before?
		if (data.armswung)
		{
			// Yes, reward him with reduction of his vl
			data.armswung = false;
			data.noswingVL *= 0.90D;
		}
		else
		{
			// No, increase vl and statistics
			data.noswingVL += 1;
			incrementStatistics(player, Id.FI_NOSWING, 1);

			// Execute whatever actions are associated with this check and the
			// violation level and find out if we should cancel the event
			cancel = executeActions(player, cc.noswingActions, data.noswingVL);
		}

		return cancel;
	}

	@Override
	public boolean isEnabled(FightConfig cc)
	{
		return cc.noswingCheck;
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
