package com.earth2me.essentials.anticheat.checks.blockplace;

import com.earth2me.essentials.anticheat.NoCheat;
import com.earth2me.essentials.anticheat.NoCheatPlayer;


public class SpeedCheck extends BlockPlaceCheck
{
	public SpeedCheck(NoCheat plugin)
	{
		super(plugin, "blockplace.speed");
	}

	public boolean check(NoCheatPlayer player, BlockPlaceData data, BlockPlaceConfig cc)
	{
		return false;
	}
}
