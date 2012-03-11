package com.earth2me.essentials.anticheat.checks.moving;

import com.earth2me.essentials.anticheat.ConfigItem;
import com.earth2me.essentials.anticheat.actions.types.ActionList;
import com.earth2me.essentials.anticheat.config.ConfPaths;
import com.earth2me.essentials.anticheat.config.NoCheatConfiguration;
import com.earth2me.essentials.anticheat.config.Permissions;


/**
 * Configurations specific for the Move Checks. Every world gets one of these assigned to it.
 *
 */
public class MovingConfig implements ConfigItem
{
	public final boolean runflyCheck;
	public final boolean identifyCreativeMode;
	public final double walkingSpeedLimit;
	public final double sprintingSpeedLimit;
	public final double jumpheight;
	public final double swimmingSpeedLimit;
	public final boolean sneakingCheck;
	public final double sneakingSpeedLimit;
	public final ActionList actions;
	public final boolean allowFlying;
	public final double flyingSpeedLimitVertical;
	public final double flyingSpeedLimitHorizontal;
	public final ActionList flyingActions;
	public final boolean nofallCheck;
	public final boolean nofallaggressive;
	public final float nofallMultiplier;
	public final ActionList nofallActions;
	public final boolean morePacketsCheck;
	public final ActionList morePacketsActions;
	public final int flyingHeightLimit;

	public MovingConfig(NoCheatConfiguration data)
	{

		identifyCreativeMode = data.getBoolean(ConfPaths.MOVING_RUNFLY_FLYING_ALLOWINCREATIVE);

		runflyCheck = data.getBoolean(ConfPaths.MOVING_RUNFLY_CHECK);

		int walkspeed = data.getInt(ConfPaths.MOVING_RUNFLY_WALKSPEED, 100);
		int sprintspeed = data.getInt(ConfPaths.MOVING_RUNFLY_SPRINTSPEED, 100);
		int swimspeed = data.getInt(ConfPaths.MOVING_RUNFLY_SWIMSPEED, 100);
		int sneakspeed = data.getInt(ConfPaths.MOVING_RUNFLY_SNEAKSPEED, 100);
		walkingSpeedLimit = (0.22 * walkspeed) / 100D;
		sprintingSpeedLimit = (0.35 * sprintspeed) / 100D;
		swimmingSpeedLimit = (0.18 * swimspeed) / 100D;
		sneakingSpeedLimit = (0.14 * sneakspeed) / 100D;
		jumpheight = ((double)135) / 100D;

		sneakingCheck = !data.getBoolean(ConfPaths.MOVING_RUNFLY_ALLOWFASTSNEAKING);
		actions = data.getActionList(ConfPaths.MOVING_RUNFLY_ACTIONS, Permissions.MOVING_RUNFLY);

		allowFlying = data.getBoolean(ConfPaths.MOVING_RUNFLY_FLYING_ALLOWALWAYS);
		flyingSpeedLimitVertical = ((double)data.getInt(ConfPaths.MOVING_RUNFLY_FLYING_SPEEDLIMITVERTICAL)) / 100D;
		flyingSpeedLimitHorizontal = ((double)data.getInt(ConfPaths.MOVING_RUNFLY_FLYING_SPEEDLIMITHORIZONTAL)) / 100D;
		flyingHeightLimit = data.getInt(ConfPaths.MOVING_RUNFLY_FLYING_HEIGHTLIMIT);
		flyingActions = data.getActionList(ConfPaths.MOVING_RUNFLY_FLYING_ACTIONS, Permissions.MOVING_FLYING);

		nofallCheck = data.getBoolean(ConfPaths.MOVING_RUNFLY_CHECKNOFALL);
		nofallMultiplier = ((float)200) / 100F;
		nofallaggressive = data.getBoolean(ConfPaths.MOVING_RUNFLY_NOFALLAGGRESSIVE);
		nofallActions = data.getActionList(ConfPaths.MOVING_RUNFLY_NOFALLACTIONS, Permissions.MOVING_NOFALL);

		morePacketsCheck = data.getBoolean(ConfPaths.MOVING_MOREPACKETS_CHECK);
		morePacketsActions = data.getActionList(ConfPaths.MOVING_MOREPACKETS_ACTIONS, Permissions.MOVING_MOREPACKETS);
	}
}
