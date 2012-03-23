package com.earth2me.essentials.anticheat.player;

import com.earth2me.essentials.anticheat.NoCheat;
import com.earth2me.essentials.anticheat.NoCheatPlayer;
import com.earth2me.essentials.anticheat.config.ConfigurationCacheStore;
import com.earth2me.essentials.anticheat.data.DataStore;
import com.earth2me.essentials.anticheat.data.ExecutionHistory;
import net.minecraft.server.EntityPlayer;
import net.minecraft.server.MobEffectList;
import org.bukkit.GameMode;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;


public class NoCheatPlayerImpl implements NoCheatPlayer
{
	private Player player;
	private final NoCheat plugin;
	private final DataStore data;
	private ConfigurationCacheStore config;
	private long lastUsedTime;
	private final ExecutionHistory history;

	public NoCheatPlayerImpl(Player player, NoCheat plugin)
	{

		this.player = player;
		this.plugin = plugin;
		this.data = new DataStore();
		this.history = new ExecutionHistory();

		this.lastUsedTime = System.currentTimeMillis();
	}

	public void refresh(Player player)
	{
		this.player = player;
		this.config = plugin.getConfig(player);
	}

	public boolean isDead()
	{
		return this.player.getHealth() <= 0 || this.player.isDead();
	}

	public boolean hasPermission(String permission)
	{
		return player.hasPermission(permission);
	}

	public DataStore getDataStore()
	{
		return data;
	}

	public ConfigurationCacheStore getConfigurationStore()
	{
		return config;
	}

	public Player getPlayer()
	{
		return player;
	}

	public String getName()
	{
		return player.getName();
	}

	public int getTicksLived()
	{
		return player.getTicksLived();
	}

	public float getSpeedAmplifier()
	{
		EntityPlayer ep = ((CraftPlayer)player).getHandle();
		if (ep.hasEffect(MobEffectList.FASTER_MOVEMENT))
		{
			// Taken directly from Minecraft code, should work
			return 1.0F + 0.2F * (float)(ep.getEffect(MobEffectList.FASTER_MOVEMENT).getAmplifier() + 1); // TODO
		}
		else
		{
			return 1.0F;
		}
	}

	@Override
	public float getJumpAmplifier()
	{
		EntityPlayer ep = ((CraftPlayer)player).getHandle();
		if (ep.hasEffect(MobEffectList.JUMP))
		{
			int amp = ep.getEffect(MobEffectList.JUMP).getAmplifier();
			// Very rough estimates only
			// TODO
			if (amp > 20)
			{
				return 1.5F * (float)(ep.getEffect(MobEffectList.JUMP).getAmplifier() + 1);
			}
			else
			{
				return 1.2F * (float)(ep.getEffect(MobEffectList.JUMP).getAmplifier() + 1);
			}
		}
		else
		{
			return 1.0F;
		}
	}

	public boolean isSprinting()
	{
		return player.isSprinting();
	}

	public void setLastUsedTime(long currentTimeInMilliseconds)
	{
		this.lastUsedTime = currentTimeInMilliseconds;
	}

	public boolean shouldBeRemoved(long currentTimeInMilliseconds)
	{
		if (lastUsedTime > currentTimeInMilliseconds)
		{
			// Should never happen, but if it does, fix it somewhat
			lastUsedTime = currentTimeInMilliseconds;
		}
		return lastUsedTime + 60000L < currentTimeInMilliseconds;
	}

	public boolean isCreative()
	{
		return player.getGameMode() == GameMode.CREATIVE || player.getAllowFlight();
	}

	@Override
	public ExecutionHistory getExecutionHistory()
	{
		return history;
	}

	@Override
	public void dealFallDamage()
	{
		EntityPlayer p = ((CraftPlayer)player).getHandle();
		p.b(0D, true);
	}
}
