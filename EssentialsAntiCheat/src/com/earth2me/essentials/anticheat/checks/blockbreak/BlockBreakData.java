package com.earth2me.essentials.anticheat.checks.blockbreak;

import com.earth2me.essentials.anticheat.DataItem;
import com.earth2me.essentials.anticheat.data.SimpleLocation;


/**
 * Player specific data for the blockbreak checks
 *
 */
public class BlockBreakData implements DataItem
{
	// Keep track of violation levels for the three checks
	public double reachVL = 0.0D;
	public double directionVL = 0.0D;
	public double noswingVL = 0.0D;
	// Used for the penalty time feature of the direction check
	public long directionLastViolationTime = 0;
	// Have a nicer/simpler way to work with block locations instead of
	// Bukkits own "Location" class
	public final SimpleLocation instaBrokenBlockLocation = new SimpleLocation();
	public final SimpleLocation brokenBlockLocation = new SimpleLocation();
	public final SimpleLocation lastDamagedBlock = new SimpleLocation();
	// indicate if the player swung his arm since he got checked last time
	public boolean armswung = true;
	// For logging, remember the reachDistance that was calculated in the
	// reach check
	public double reachDistance;
}
