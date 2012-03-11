package com.earth2me.essentials.anticheat.checks.fight;

import com.earth2me.essentials.anticheat.ConfigItem;
import com.earth2me.essentials.anticheat.actions.types.ActionList;
import com.earth2me.essentials.anticheat.config.ConfPaths;
import com.earth2me.essentials.anticheat.config.NoCheatConfiguration;
import com.earth2me.essentials.anticheat.config.Permissions;


/**
 * Configurations specific for the "Fight" checks Every world gets one of these assigned to it, or if a world doesn't
 * get it's own, it will use the "global" version
 *
 */
public class FightConfig implements ConfigItem
{
	public final boolean directionCheck;
	public final double directionPrecision;
	public final ActionList directionActions;
	public final long directionPenaltyTime;
	public final boolean noswingCheck;
	public final ActionList noswingActions;
	public final boolean reachCheck;
	public final double reachLimit;
	public final long reachPenaltyTime;
	public final ActionList reachActions;
	public final int speedAttackLimit;
	public final ActionList speedActions;
	public final boolean speedCheck;
	public final boolean godmodeCheck;
	public final ActionList godmodeActions;
	public final boolean instanthealCheck;
	public final ActionList instanthealActions;

	public FightConfig(NoCheatConfiguration data)
	{

		directionCheck = data.getBoolean(ConfPaths.FIGHT_DIRECTION_CHECK);
		directionPrecision = ((double)(data.getInt(ConfPaths.FIGHT_DIRECTION_PRECISION))) / 100D;
		directionPenaltyTime = data.getInt(ConfPaths.FIGHT_DIRECTION_PENALTYTIME);
		directionActions = data.getActionList(ConfPaths.FIGHT_DIRECTION_ACTIONS, Permissions.FIGHT_DIRECTION);
		noswingCheck = data.getBoolean(ConfPaths.FIGHT_NOSWING_CHECK);
		noswingActions = data.getActionList(ConfPaths.FIGHT_NOSWING_ACTIONS, Permissions.FIGHT_NOSWING);
		reachCheck = data.getBoolean(ConfPaths.FIGHT_REACH_CHECK);
		reachLimit = ((double)(data.getInt(ConfPaths.FIGHT_REACH_LIMIT))) / 100D;
		reachPenaltyTime = data.getInt(ConfPaths.FIGHT_REACH_PENALTYTIME);
		reachActions = data.getActionList(ConfPaths.FIGHT_REACH_ACTIONS, Permissions.FIGHT_REACH);
		speedCheck = data.getBoolean(ConfPaths.FIGHT_SPEED_CHECK);
		speedActions = data.getActionList(ConfPaths.FIGHT_SPEED_ACTIONS, Permissions.FIGHT_SPEED);
		speedAttackLimit = data.getInt(ConfPaths.FIGHT_SPEED_ATTACKLIMIT);

		godmodeCheck = data.getBoolean(ConfPaths.FIGHT_GODMODE_CHECK);
		godmodeActions = data.getActionList(ConfPaths.FIGHT_GODMODE_ACTIONS, Permissions.FIGHT_GODMODE);

		instanthealCheck = data.getBoolean(ConfPaths.FIGHT_INSTANTHEAL_CHECK);
		instanthealActions = data.getActionList(ConfPaths.FIGHT_INSTANTHEAL_ACTIONS, Permissions.FIGHT_INSTANTHEAL);
	}
}
