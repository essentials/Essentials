package com.earth2me.essentials.anticheat.checks.fight;

import com.earth2me.essentials.anticheat.NoCheat;
import com.earth2me.essentials.anticheat.NoCheatPlayer;
import com.earth2me.essentials.anticheat.actions.ParameterName;
import com.earth2me.essentials.anticheat.config.Permissions;
import com.earth2me.essentials.anticheat.data.Statistics.Id;
import java.util.Locale;


/**
 * The speed check will find out if a player interacts with something that's too far away
 *
 */
public class SpeedCheck extends FightCheck
{
	public SpeedCheck(NoCheat plugin)
	{
		super(plugin, "fight.speed", Permissions.FIGHT_SPEED);
	}

	public boolean check(NoCheatPlayer player, FightData data, FightConfig cc)
	{

		boolean cancel = false;

		final long time = System.currentTimeMillis();

		// Check if one second has passed and reset counters and vl in that case
		if (data.speedTime + 1000L <= time)
		{
			data.speedTime = time;
			data.speedAttackCount = 0;
			data.speedVL = 0;
		}

		// count the attack
		data.speedAttackCount++;

		// too many attacks
		if (data.speedAttackCount > cc.speedAttackLimit)
		{
			// if there was lag, don't count it towards statistics and vl
			if (!plugin.skipCheck())
			{
				data.speedVL += 1;
				incrementStatistics(player, Id.FI_SPEED, 1);
			}

			// Execute whatever actions are associated with this check and the
			// violation level and find out if we should cancel the event
			cancel = executeActions(player, cc.speedActions, data.speedVL);
		}

		return cancel;
	}

	@Override
	public boolean isEnabled(FightConfig cc)
	{
		return cc.speedCheck;
	}

	@Override
	public String getParameter(ParameterName wildcard, NoCheatPlayer player)
	{

		if (wildcard == ParameterName.VIOLATIONS)
		{
			return String.format(Locale.US, "%d", getData(player).speedVL);
		}
		else if (wildcard == ParameterName.LIMIT)
		{
			return String.format(Locale.US, "%d", getConfig(player.getConfigurationStore()).speedAttackLimit);
		}
		else
		{
			return super.getParameter(wildcard, player);
		}
	}
}
