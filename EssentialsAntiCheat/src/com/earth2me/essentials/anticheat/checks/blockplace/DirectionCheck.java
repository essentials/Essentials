package com.earth2me.essentials.anticheat.checks.blockplace;

import com.earth2me.essentials.anticheat.NoCheat;
import com.earth2me.essentials.anticheat.NoCheatPlayer;
import com.earth2me.essentials.anticheat.actions.ParameterName;
import com.earth2me.essentials.anticheat.checks.CheckUtil;
import com.earth2me.essentials.anticheat.data.SimpleLocation;
import com.earth2me.essentials.anticheat.data.Statistics.Id;
import java.util.Locale;
import org.bukkit.Location;


/**
 * The DirectionCheck will find out if a player tried to interact with something that's not in his field of view.
 *
 */
public class DirectionCheck extends BlockPlaceCheck
{
	public DirectionCheck(NoCheat plugin)
	{
		super(plugin, "blockplace.direction");
	}

	public boolean check(NoCheatPlayer player, BlockPlaceData data, BlockPlaceConfig cc)
	{

		boolean cancel = false;

		final SimpleLocation blockPlaced = data.blockPlaced;
		final SimpleLocation blockPlacedAgainst = data.blockPlacedAgainst;

		// How far "off" is the player with his aim. We calculate from the
		// players eye location and view direction to the center of the target
		// block. If the line of sight is more too far off, "off" will be
		// bigger than 0
		double off = CheckUtil.directionCheck(player, blockPlacedAgainst.x + 0.5D, blockPlacedAgainst.y + 0.5D, blockPlacedAgainst.z + 0.5D, 1D, 1D, cc.directionPrecision);

		// now check if the player is looking at the block from the correct side
		double off2 = 0.0D;

		// Find out against which face the player tried to build, and if he
		// stood on the correct side of it
		Location eyes = player.getPlayer().getEyeLocation();
		if (blockPlaced.x > blockPlacedAgainst.x)
		{
			off2 = blockPlacedAgainst.x + 0.5D - eyes.getX();
		}
		else if (blockPlaced.x < blockPlacedAgainst.x)
		{
			off2 = -(blockPlacedAgainst.x + 0.5D - eyes.getX());
		}
		else if (blockPlaced.y > blockPlacedAgainst.y)
		{
			off2 = blockPlacedAgainst.y + 0.5D - eyes.getY();
		}
		else if (blockPlaced.y < blockPlacedAgainst.y)
		{
			off2 = -(blockPlacedAgainst.y + 0.5D - eyes.getY());
		}
		else if (blockPlaced.z > blockPlacedAgainst.z)
		{
			off2 = blockPlacedAgainst.z + 0.5D - eyes.getZ();
		}
		else if (blockPlaced.z < blockPlacedAgainst.z)
		{
			off2 = -(blockPlacedAgainst.z + 0.5D - eyes.getZ());
		}

		// If he wasn't on the correct side, add that to the "off" value
		if (off2 > 0.0D)
		{
			off += off2;
		}

		final long time = System.currentTimeMillis();

		if (off < 0.1D)
		{
			// Player did nothing wrong
			// reduce violation counter to reward him
			data.directionVL *= 0.9D;
		}
		else
		{
			// Player failed the check
			// Increment violation counter and statistics
			data.directionVL += off;
			incrementStatistics(player, Id.BP_DIRECTION, off);

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
