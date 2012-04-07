package com.earth2me.essentials.anticheat.checks.blockplace;

import com.earth2me.essentials.anticheat.ConfigItem;
import com.earth2me.essentials.anticheat.actions.types.ActionList;
import com.earth2me.essentials.anticheat.config.ConfPaths;
import com.earth2me.essentials.anticheat.config.NoCheatConfiguration;
import com.earth2me.essentials.anticheat.config.Permissions;


/**
 * Configurations specific for the "BlockPlace" checks Every world gets one of these assigned to it, or if a world
 * doesn't get it's own, it will use the "global" version
 *
 */
public class BlockPlaceConfig implements ConfigItem
{
	public final boolean reachCheck;
	public final double reachDistance;
	public final ActionList reachActions;
	public final boolean directionCheck;
	public final ActionList directionActions;
	public final long directionPenaltyTime;
	public final double directionPrecision;
	public final boolean speedCheck;
	public final int speedTime;
	public final ActionList speedActions;

	public BlockPlaceConfig(NoCheatConfiguration data)
	{
		speedCheck = data.getBoolean(ConfPaths.BLOCKPLACE_SPEED_CHECK);
		speedTime = data.getInt(ConfPaths.BLOCKPLACE_SPEED_TIME);
		speedActions = data.getActionList(ConfPaths.BLOCKPLACE_SPEED_ACTIONS, Permissions.BLOCKPLACE_SPEED);

		reachCheck = data.getBoolean(ConfPaths.BLOCKPLACE_REACH_CHECK);
		reachDistance = 535D / 100D;
		reachActions = data.getActionList(ConfPaths.BLOCKPLACE_REACH_ACTIONS, Permissions.BLOCKPLACE_REACH);

		directionCheck = data.getBoolean(ConfPaths.BLOCKPLACE_DIRECTION_CHECK);
		directionPenaltyTime = data.getInt(ConfPaths.BLOCKPLACE_DIRECTION_PENALTYTIME);
		directionPrecision = ((double)data.getInt(ConfPaths.BLOCKPLACE_DIRECTION_PRECISION)) / 100D;
		directionActions = data.getActionList(ConfPaths.BLOCKPLACE_DIRECTION_ACTIONS, Permissions.BLOCKPLACE_DIRECTION);
	}
}
