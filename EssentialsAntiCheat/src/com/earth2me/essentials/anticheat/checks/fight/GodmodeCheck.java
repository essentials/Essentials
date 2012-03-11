package com.earth2me.essentials.anticheat.checks.fight;

import com.earth2me.essentials.anticheat.NoCheat;
import com.earth2me.essentials.anticheat.NoCheatPlayer;
import com.earth2me.essentials.anticheat.actions.ParameterName;
import com.earth2me.essentials.anticheat.config.Permissions;
import com.earth2me.essentials.anticheat.data.Statistics;
import java.util.Locale;
import net.minecraft.server.EntityPlayer;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.entity.CraftPlayer;


/**
 * The Godmode Check will find out if a player tried to stay invulnerable after being hit or after dying
 *
 */
public class GodmodeCheck extends FightCheck
{
	public GodmodeCheck(NoCheat plugin)
	{
		super(plugin, "fight.godmode", Permissions.FIGHT_GODMODE);
	}

	@Override
	public boolean check(NoCheatPlayer player, FightData data, FightConfig cc)
	{

		boolean cancelled = false;

		long time = System.currentTimeMillis();

		// Check at most once a second
		if (data.godmodeLastDamageTime + 1000L < time)
		{
			data.godmodeLastDamageTime = time;

			// How old is the player now?
			int age = player.getTicksLived();
			// How much older did he get?
			int ageDiff = Math.max(0, age - data.godmodeLastAge);
			// Is he invulnerable?
			int nodamageTicks = player.getPlayer().getNoDamageTicks();

			if (nodamageTicks > 0 && ageDiff < 15)
			{
				// He is invulnerable and didn't age fast enough, that costs
				// some points
				data.godmodeBuffer -= (15 - ageDiff);

				// Still points left?
				if (data.godmodeBuffer <= 0)
				{
					// No, that means VL and statistics increased
					data.godmodeVL -= data.godmodeBuffer;
					incrementStatistics(player, Statistics.Id.FI_GODMODE, -data.godmodeBuffer);

					// Execute whatever actions are associated with this check and the
					// violation level and find out if we should cancel the event
					cancelled = executeActions(player, cc.godmodeActions, data.godmodeVL);
				}
			}
			else
			{
				// Give some new points, once a second
				data.godmodeBuffer += 15;
				data.godmodeVL *= 0.95;
			}

			if (data.godmodeBuffer < 0)
			{
				// Can't have less than 0
				data.godmodeBuffer = 0;
			}
			else if (data.godmodeBuffer > 30)
			{
				// And 30 is enough for simple lag situations
				data.godmodeBuffer = 30;
			}

			// Start age counting from a new time
			data.godmodeLastAge = age;
		}

		return cancelled;
	}

	@Override
	public boolean isEnabled(FightConfig cc)
	{
		return cc.godmodeCheck;
	}

	@Override
	public String getParameter(ParameterName wildcard, NoCheatPlayer player)
	{

		if (wildcard == ParameterName.VIOLATIONS)
		{
			return String.format(Locale.US, "%d", (int)getData(player).godmodeVL);
		}
		else
		{
			return super.getParameter(wildcard, player);
		}
	}

	/**
	 * If a player apparently died, make sure he really dies after some time if he didn't already, by setting up a
	 * Bukkit task
	 *
	 * @param player The player
	 */
	public void death(CraftPlayer player)
	{
		// First check if the player is really dead (e.g. another plugin could
		// have just fired an artificial event)
		if (player.getHealth() <= 0 && player.isDead())
		{
			try
			{
				final EntityPlayer entity = player.getHandle();

				// Schedule a task to be executed in roughly 1.5 seconds
				Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable()
				{
					public void run()
					{
						try
						{
							// Check again if the player should be dead, and
							// if the game didn't mark him as dead
							if (entity.getHealth() <= 0 && !entity.dead)
							{
								// Artifically "kill" him
								entity.deathTicks = 19;
								entity.a(true);
							}
						}
						catch (Exception e)
						{
						}
					}
				}, 30);
			}
			catch (Exception e)
			{
			}
		}
	}
}
