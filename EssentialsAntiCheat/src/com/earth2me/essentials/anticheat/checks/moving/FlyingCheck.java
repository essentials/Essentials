package com.earth2me.essentials.anticheat.checks.moving;

import com.earth2me.essentials.anticheat.NoCheat;
import com.earth2me.essentials.anticheat.NoCheatPlayer;
import com.earth2me.essentials.anticheat.actions.ParameterName;
import com.earth2me.essentials.anticheat.data.PreciseLocation;
import com.earth2me.essentials.anticheat.data.Statistics.Id;
import java.util.Locale;


/**
 * A check designed for people that are allowed to fly. The complement to the "RunningCheck", which is for people that
 * aren't allowed to fly, and therefore have tighter rules to obey.
 *
 */
public class FlyingCheck extends MovingCheck
{
	public FlyingCheck(NoCheat plugin)
	{
		super(plugin, "moving.flying");
	}
	// Determined by trial and error, the flying movement speed of the creative
	// mode
	private static final double creativeSpeed = 0.60D;

	public PreciseLocation check(NoCheatPlayer player, MovingData data, MovingConfig ccmoving)
	{

		// The setBack is the location that players may get teleported to when
		// they fail the check
		final PreciseLocation setBack = data.runflySetBackPoint;

		final PreciseLocation from = data.from;
		final PreciseLocation to = data.to;

		// If we have no setback, define one now
		if (!setBack.isSet())
		{
			setBack.set(from);
		}

		// Used to store the location where the player gets teleported to
		PreciseLocation newToLocation = null;

		// Before doing anything, do a basic height check to determine if
		// players are flying too high
		int maxheight = ccmoving.flyingHeightLimit + player.getPlayer().getWorld().getMaxHeight();

		if (to.y - data.vertFreedom > maxheight)
		{
			newToLocation = new PreciseLocation();
			newToLocation.set(setBack);
			newToLocation.y = maxheight - 10;
			return newToLocation;
		}

		// Calculate some distances
		final double yDistance = to.y - from.y;
		final double xDistance = to.x - from.x;
		final double zDistance = to.z - from.z;

		// How far did the player move horizontally
		final double horizontalDistance = Math.sqrt((xDistance * xDistance + zDistance * zDistance));

		double resultHoriz = 0;
		double resultVert = 0;
		double result = 0;

		// In case of creative game mode give at least 0.60 speed limit horizontal
		double speedLimitHorizontal = player.isCreative() ? Math.max(creativeSpeed, ccmoving.flyingSpeedLimitHorizontal) : ccmoving.flyingSpeedLimitHorizontal;

		// If the player is affected by potion of swiftness
		speedLimitHorizontal *= player.getSpeedAmplifier();

		// Finally, determine how far the player went beyond the set limits
		resultHoriz = Math.max(0.0D, horizontalDistance - data.horizFreedom - speedLimitHorizontal);

		boolean sprinting = player.isSprinting();

		data.bunnyhopdelay--;

		if (resultHoriz > 0 && sprinting)
		{

			// Try to treat it as a the "bunnyhop" problem
			// The bunnyhop problem is that landing and immediatly jumping
			// again leads to a player moving almost twice as far in that step
			if (data.bunnyhopdelay <= 0 && resultHoriz < 0.4D)
			{
				data.bunnyhopdelay = 9;
				resultHoriz = 0;
			}
		}

		resultHoriz *= 100;

		// Is the player affected by the "jumping" potion
		// This is really just a very, very crude estimation and far from
		// reality
		double jumpAmplifier = player.getJumpAmplifier();
		if (jumpAmplifier > data.lastJumpAmplifier)
		{
			data.lastJumpAmplifier = jumpAmplifier;
		}

		double speedLimitVertical = ccmoving.flyingSpeedLimitVertical * data.lastJumpAmplifier;

		if (data.from.y >= data.to.y && data.lastJumpAmplifier > 0)
		{
			data.lastJumpAmplifier--;
		}

		// super simple, just check distance compared to max distance vertical
		resultVert = Math.max(0.0D, yDistance - data.vertFreedom - speedLimitVertical) * 100;

		result = resultHoriz + resultVert;

		// The player went to far, either horizontal or vertical
		if (result > 0)
		{

			// Increment violation counter and statistics
			data.runflyVL += result;
			if (resultHoriz > 0)
			{
				incrementStatistics(player, Id.MOV_RUNNING, resultHoriz);
			}

			if (resultVert > 0)
			{
				incrementStatistics(player, Id.MOV_FLYING, resultVert);
			}

			// Execute whatever actions are associated with this check and the
			// violation level and find out if we should cancel the event
			boolean cancel = executeActions(player, ccmoving.flyingActions, data.runflyVL);

			// Was one of the actions a cancel? Then really do it
			if (cancel)
			{
				newToLocation = setBack;
			}
		}

		// Slowly reduce the violation level with each event
		data.runflyVL *= 0.97;

		// If the player did not get cancelled, define a new setback point
		if (newToLocation == null)
		{
			setBack.set(to);
		}

		return newToLocation;
	}

	@Override
	public String getParameter(ParameterName wildcard, NoCheatPlayer player)
	{

		if (wildcard == ParameterName.VIOLATIONS)
		{
			return String.format(Locale.US, "%d", (int)getData(player).runflyVL);
		}
		else
		{
			return super.getParameter(wildcard, player);
		}
	}
}
