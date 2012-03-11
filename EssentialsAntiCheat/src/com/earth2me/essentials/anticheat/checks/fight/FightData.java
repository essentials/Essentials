package com.earth2me.essentials.anticheat.checks.fight;

import com.earth2me.essentials.anticheat.DataItem;
import net.minecraft.server.Entity;


/**
 * Player specific data for the fight checks
 *
 */
public class FightData implements DataItem
{
	// Keep track of the violation levels of the checks
	public double directionVL;
	public double noswingVL;
	public double reachVL;
	public int speedVL;
	public double godmodeVL;
	public double instanthealVL;
	// For checks that have penalty time
	public long directionLastViolationTime;
	public long reachLastViolationTime;
	// godmode check needs to know these
	public long godmodeLastDamageTime;
	public int godmodeLastAge;
	public int godmodeBuffer = 40;
	// last time player regenerated health by satiation
	public long instanthealLastRegenTime;
	// three seconds buffer to smooth out lag
	public long instanthealBuffer = 3000;
	// While handling an event, use this to keep the attacked entity
	public Entity damagee;
	// The player swung his arm
	public boolean armswung = true;
	// For some reason the next event should be ignored
	public boolean skipNext = false;
	// Keep track of time and amount of attacks
	public long speedTime;
	public int speedAttackCount;
}
