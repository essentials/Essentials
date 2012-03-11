package com.earth2me.essentials.anticheat.checks.blockbreak;

import java.util.Locale;
import com.earth2me.essentials.anticheat.NoCheat;
import com.earth2me.essentials.anticheat.NoCheatPlayer;
import com.earth2me.essentials.anticheat.actions.ParameterName;
import com.earth2me.essentials.anticheat.checks.CheckUtil;
import com.earth2me.essentials.anticheat.data.SimpleLocation;
import com.earth2me.essentials.anticheat.data.Statistics.Id;


/**
 * The DirectionCheck will find out if a player tried to interact with something that's not in his field of view.
 *
 */
public class DirectionCheck extends BlockBreakCheck
{
	public DirectionCheck(NoCheat plugin)
	{
		super(plugin, "blockbreak.direction");
	}

	public boolean check(final NoCheatPlayer player, final BlockBreakData data, final BlockBreakConfig ccblockbreak)
	{

		final SimpleLocation brokenBlock = data.brokenBlockLocation;
		boolean cancel = false;

		// How far "off" is the player with his aim. We calculate from the
		// players eye location and view direction to the center of the target
		// block. If the line of sight is more too far off, "off" will be
		// bigger than 0
		double off = CheckUtil.directionCheck(player, brokenBlock.x + 0.5D, brokenBlock.y + 0.5D, brokenBlock.z + 0.5D, 1D, 1D, ccblockbreak.directionPrecision);

		final long time = System.currentTimeMillis();

		if (off < 0.1D)
		{
			// Player did likely nothing wrong
			// reduce violation counter to reward him
			data.directionVL *= 0.9D;
		}
		else
		{
			// Player failed the check
			// Increment violation counter
			if (data.instaBrokenBlockLocation.equals(brokenBlock))
			{
				// Instabreak block failures are very common, so don't be as
				// hard on people failing them
				off /= 5;
			}

			// Add to the overall violation level of the check and add to
			// statistics
			data.directionVL += off;
			incrementStatistics(player, Id.BB_DIRECTION, off);

			// Execute whatever actions are associated with this check and the
			// violation level and find out if we should cancel the event
			cancel = executeActions(player, ccblockbreak.directionActions, data.directionVL);

			if (cancel)
			{
				// if we should cancel, remember the current time too
				data.directionLastViolationTime = time;
			}
		}

		// If the player is still in penalty time, cancel the event anyway
		if (data.directionLastViolationTime + ccblockbreak.directionPenaltyTime > time)
		{
			// A saveguard to avoid people getting stuck in penalty time
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
