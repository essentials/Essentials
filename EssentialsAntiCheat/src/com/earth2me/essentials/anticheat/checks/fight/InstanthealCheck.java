package com.earth2me.essentials.anticheat.checks.fight;

import com.earth2me.essentials.anticheat.NoCheat;
import com.earth2me.essentials.anticheat.NoCheatPlayer;
import com.earth2me.essentials.anticheat.actions.ParameterName;
import com.earth2me.essentials.anticheat.config.Permissions;
import com.earth2me.essentials.anticheat.data.Statistics;
import java.util.Locale;


/**
 * The instantheal Check should find out if a player tried to artificially accellerate the health regeneration by food
 *
 */
public class InstanthealCheck extends FightCheck
{
	public InstanthealCheck(NoCheat plugin)
	{
		super(plugin, "fight.instantheal", Permissions.FIGHT_INSTANTHEAL);
	}

	@Override
	public boolean check(NoCheatPlayer player, FightData data, FightConfig cc)
	{

		boolean cancelled = false;

		long time = System.currentTimeMillis();

		// security check if system time ran backwards
		if (data.instanthealLastRegenTime > time)
		{
			data.instanthealLastRegenTime = 0;
			return false;
		}

		long difference = time - (data.instanthealLastRegenTime + 3500L);

		data.instanthealBuffer += difference;

		if (data.instanthealBuffer < 0)
		{
			// Buffer has been fully consumed
			// Increase vl and statistics
			double vl = data.instanthealVL -= data.instanthealBuffer / 1000;
			incrementStatistics(player, Statistics.Id.FI_INSTANTHEAL, vl);

			data.instanthealBuffer = 0;

			// Execute whatever actions are associated with this check and the
			// violation level and find out if we should cancel the event
			cancelled = executeActions(player, cc.instanthealActions, data.instanthealVL);
		}
		else
		{
			// vl gets decreased
			data.instanthealVL *= 0.9;
		}

		// max 2 seconds buffer
		if (data.instanthealBuffer > 2000L)
		{
			data.instanthealBuffer = 2000L;
		}

		if (!cancelled)
		{
			// New reference time
			data.instanthealLastRegenTime = time;
		}

		return cancelled;
	}

	@Override
	public boolean isEnabled(FightConfig cc)
	{
		return cc.instanthealCheck;
	}

	@Override
	public String getParameter(ParameterName wildcard, NoCheatPlayer player)
	{

		if (wildcard == ParameterName.VIOLATIONS)
		{
			return String.format(Locale.US, "%d", (int)getData(player).instanthealVL);
		}
		else
		{
			return super.getParameter(wildcard, player);
		}
	}
}
