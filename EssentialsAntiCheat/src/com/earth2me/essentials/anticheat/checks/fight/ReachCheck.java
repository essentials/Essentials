package com.earth2me.essentials.anticheat.checks.fight;

import com.earth2me.essentials.anticheat.NoCheat;
import com.earth2me.essentials.anticheat.NoCheatPlayer;
import com.earth2me.essentials.anticheat.actions.ParameterName;
import com.earth2me.essentials.anticheat.checks.CheckUtil;
import com.earth2me.essentials.anticheat.config.Permissions;
import com.earth2me.essentials.anticheat.data.Statistics.Id;
import java.util.Locale;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityComplex;
import net.minecraft.server.EntityComplexPart;


/**
 * The reach check will find out if a player interacts with something that's too far away
 *
 */
public class ReachCheck extends FightCheck
{
	public ReachCheck(NoCheat plugin)
	{
		super(plugin, "fight.reach", Permissions.FIGHT_REACH);
	}

	public boolean check(NoCheatPlayer player, FightData data, FightConfig cc)
	{

		boolean cancel = false;

		final long time = System.currentTimeMillis();

		// Get the width of the damagee
		Entity entity = data.damagee;

		// Safeguard, if entity is Giant or Ender Dragon, this check will fail
		// due to giant and hard to define hitboxes
		if (entity instanceof EntityComplex || entity instanceof EntityComplexPart)
		{
			return false;
		}

		// Distance is calculated from eye location to center of targeted
		// If the player is further away from his target than allowed, the
		// difference will be assigned to "distance"
		final double off = CheckUtil.reachCheck(player, entity.locX, entity.locY + 1.0D, entity.locZ, cc.reachLimit);

		if (off < 0.1D)
		{
			// Player did probably nothing wrong
			// reduce violation counter to reward him
			data.reachVL *= 0.80D;
		}
		else
		{
			// Player failed the check
			// Increment violation counter and statistics
			// This is influenced by lag, so don't do it if there was lag
			if (!plugin.skipCheck())
			{
				double sqrt = Math.sqrt(off);
				data.reachVL += sqrt;
				incrementStatistics(player, Id.FI_REACH, sqrt);
			}

			// Execute whatever actions are associated with this check and the
			// violation level and find out if we should cancel the event
			cancel = executeActions(player, cc.reachActions, data.reachVL);

			if (cancel)
			{
				// if we should cancel, remember the current time too
				data.reachLastViolationTime = time;
			}
		}

		// If the player is still in penalty time, cancel the event anyway
		if (data.reachLastViolationTime + cc.reachPenaltyTime > time)
		{
			// A safeguard to avoid people getting stuck in penalty time
			// indefinitely in case the system time of the server gets changed
			if (data.reachLastViolationTime > time)
			{
				data.reachLastViolationTime = 0;
			}

			// He is in penalty time, therefore request cancelling of the event
			return true;
		}

		return cancel;
	}

	@Override
	public boolean isEnabled(FightConfig cc)
	{
		return cc.reachCheck;
	}

	@Override
	public String getParameter(ParameterName wildcard, NoCheatPlayer player)
	{

		if (wildcard == ParameterName.VIOLATIONS)
		{
			return String.format(Locale.US, "%d", (int)getData(player).reachVL);
		}
		else
		{
			return super.getParameter(wildcard, player);
		}
	}
}
