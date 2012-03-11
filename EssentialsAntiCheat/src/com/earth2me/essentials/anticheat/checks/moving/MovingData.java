package com.earth2me.essentials.anticheat.checks.moving;

import com.earth2me.essentials.anticheat.DataItem;
import com.earth2me.essentials.anticheat.data.PreciseLocation;
import com.earth2me.essentials.anticheat.data.Statistics.Id;


/**
 * Player specific data for the moving check group
 */
public class MovingData implements DataItem
{
	// Keep track of the violation levels of the checks
	public double runflyVL;
	public double nofallVL;
	public double morePacketsVL;
	// Count how long a player is in the air
	public int jumpPhase;
	// Remember how big the players last JumpAmplifier (potion effect) was
	public double lastJumpAmplifier;
	// Remember for a short time that the player was on ice and therefore
	// should be allowed to move a bit faster
	public int onIce;
	// Where should a player be teleported back to when failing the check
	public final PreciseLocation runflySetBackPoint = new PreciseLocation();
	// Some values for estimating movement freedom
	public double vertFreedom;
	public double vertVelocity;
	public int vertVelocityCounter;
	public double horizFreedom;
	public int horizVelocityCounter;
	public double horizontalBuffer;
	public int bunnyhopdelay;
	// Keep track of estimated fall distance to compare to real fall distance
	public float fallDistance;
	public float lastAddedFallDistance;
	// Keep track of when "morePackets" last time checked and how much packets
	// a player sent and may send before failing the check
	public long morePacketsLastTime;
	public int packets;
	public int morePacketsBuffer = 50;
	// Where to teleport the player that fails the "morepackets" check
	public final PreciseLocation morePacketsSetbackPoint = new PreciseLocation();
	// When NoCheat does teleport the player, remember the target location to
	// be able to distinguish "our" teleports from teleports of others
	public final PreciseLocation teleportTo = new PreciseLocation();
	// For logging and convenience, make copies of the events locations
	public final PreciseLocation from = new PreciseLocation();
	public final PreciseLocation to = new PreciseLocation();
	// For convenience, remember if the locations are considered "on ground"
	// by NoCheat
	public boolean fromOnOrInGround;
	public boolean toOnOrInGround;
	public Id statisticCategory = Id.MOV_RUNNING;

	public void clearRunFlyData()
	{
		runflySetBackPoint.reset();
		jumpPhase = 0;
		fallDistance = 0;
		lastAddedFallDistance = 0;
		bunnyhopdelay = 0;
	}

	public void clearMorePacketsData()
	{
		morePacketsSetbackPoint.reset();
	}
}
