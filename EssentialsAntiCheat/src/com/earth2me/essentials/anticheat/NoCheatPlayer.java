package com.earth2me.essentials.anticheat;

import com.earth2me.essentials.anticheat.config.ConfigurationCacheStore;
import com.earth2me.essentials.anticheat.data.DataStore;
import com.earth2me.essentials.anticheat.data.ExecutionHistory;
import org.bukkit.entity.Player;


public interface NoCheatPlayer
{
	public boolean hasPermission(String permission);

	public String getName();

	public Player getPlayer();

	public DataStore getDataStore();

	public boolean isDead();

	public boolean isSprinting();

	public int getTicksLived();

	public ConfigurationCacheStore getConfigurationStore();

	public float getSpeedAmplifier();

	public float getJumpAmplifier();

	public boolean isCreative();

	public ExecutionHistory getExecutionHistory();

	public void dealFallDamage();
}
