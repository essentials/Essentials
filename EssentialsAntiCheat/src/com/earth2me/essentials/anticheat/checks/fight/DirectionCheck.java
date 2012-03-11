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
 * The DirectionCheck will find out if a player tried to interact with something that's not in his field of view.
 *
 */
public class DirectionCheck extends FightCheck
{
	public DirectionCheck(NoCheat plugin)
	{
		super(plugin, "fight.direction", Permissions.FIGHT_DIRECTION);
	}

	public boolean check(NoCheatPlayer player, FightData data, FightConfig cc)
	{

		boolean cancel = false;

		final long time = System.currentTimeMillis();

		// Get the damagee (entity that got hit)
		Entity entity = data.damagee;

		// Safeguard, if entity is complex, this check will fail
		// due to giant and hard to define hitboxes
		if (entity instanceof EntityComplex || entity instanceof EntityComplexPart)
		{
			return false;
		}

		// Find out how wide the entity is
		final float width = entity.length > entity.width ? entity.length : entity.width;
		// entity.height is broken and will always be 0, therefore
		// calculate height instead based on boundingBox
		final double height = entity.boundingBox.e - entity.boundingBox.b;

		// How far "off" is the player with his aim. We calculate from the
		// players eye location and view direction to the center of the target
		// entity. If the line of sight is more too far off, "off" will be
		// bigger than 0
		final double off = CheckUtil.directionCheck(player, entity.locX, entity.locY + (height / 2D), entity.locZ, width, height, cc.directionPrecision);

		if (off < 0.1D)
		{
			// Player did probably nothing wrong
			// reduce violation counter to reward him
			data.directionVL *= 0.80D;
		}
		else
		{
			// Player failed the check
			// Increment violation counter and statistics, but only if there
			// wasn't serious lag
			if (!plugin.skipCheck())
			{
				double sqrt = Math.sqrt(off);
				data.directionVL += sqrt;
				incrementStatistics(player, Id.FI_DIRECTION, sqrt);
			}

			// Execute whatever actions are associated with this check and the
			// violation level and find out if we should cancel the event
			cancel = executeActions(player, cc.directionActions, data.directionVL);

			if (cancel)
			{
				// if we should cancel, remember the current time too
				data.directionLastViolationTime = time;
			}
		}

		// If the player is still in penalty time, cancel the event anyway
		if (data.directionLastViolationTime + cc.directionPenaltyTime > time)
		{
			// A safeguard to avoid people getting stuck in penalty time
			// indefinitely in case the system time of the server gets changed
			if (data.directionLastViolationTime > time)
			{
				data.directionLastViolationTime = 0;
			}

			// He is in penalty time, therefore request cancelling of the event
			return true;
		}

		return cancel;
	}

	@Override
	public boolean isEnabled(FightConfig cc)
	{
		return cc.directionCheck;
	}

	@Override
	public String getParameter(ParameterName wildcard, NoCheatPlayer player)
	{

		if (wildcard == ParameterName.VIOLATIONS)
		{
			return String.format(Locale.US, "%d", (int)getData(player).directionVL);
		}
		else
		{
			return super.getParameter(wildcard, player);
		}
	}
}
